package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.domain.model.IssueEvent;
import ee.ria.riha.domain.model.IssueStatus;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class IssueServiceTest {

    private static final String EXISTING_INFO_SYSTEM_SHORT_NAME = "is1";
    private static final Long EXISTING_ISSUE_ID = 15503L;
    private static final Long CREATED_COMMENT_ID = 111L;

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    private RihaOrganizationAwareAuthenticationToken authenticationToken =
            JaneAuthenticationTokenBuilder.builder().build();

    @Mock
    private InfoSystemService infoSystemService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueEventService issueEventService;

    @Mock
    private IssueCommentService issueCommentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private IssueService issueService;

    private InfoSystem existingInfoSystem = new InfoSystem(
            "{\n" +
                    "  \"short_name\": \"" + EXISTING_INFO_SYSTEM_SHORT_NAME + "\"\n" +
                    "}");

    private Issue existingIssue = Issue.builder()
            .id(EXISTING_ISSUE_ID)
            .status(IssueStatus.OPEN)
            .build();

    private Comment existingIssueEntity = IssueService.ISSUE_TO_COMMENT.apply(existingIssue);


    @Before
    public void setUp() {
        // Reset authorization
        authenticationToken.setActiveOrganization("555010203");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(infoSystemService.get(EXISTING_INFO_SYSTEM_SHORT_NAME)).thenReturn(existingInfoSystem);

        when(commentRepository.get(EXISTING_ISSUE_ID)).thenReturn(existingIssueEntity);
        when(commentRepository.add(any(Comment.class))).thenReturn(Arrays.asList(CREATED_COMMENT_ID));

        doNothing().when(notificationService).sendNewIssueToSystemContactsNotification(any(InfoSystem.class));
        doNothing().when(notificationService).sendNewIssueToApproversNotification(any(String.class), any(InfoSystem.class));
    }

    @Test
    public void createsIssueWithTitleAndComment() {
        issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, "critical issue",
                                           "clear problem description");

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).add(commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getTitle(), is(equalTo("critical issue")));
        assertThat(comment.getComment(), is(equalTo("clear problem description")));
    }

    @Test
    public void populatesAuthorAndOrganizationFromUserDetailsDuringIssueCreation() {
        issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, "title", "comment");

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).add(commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getAuthor_name(), is(equalTo("Jane Doe")));
        assertThat(comment.getAuthor_personal_code(), is(equalTo("EE40102031234")));
        assertThat(comment.getOrganization_name(), is(equalTo("Acme org")));
        assertThat(comment.getOrganization_code(), is(equalTo("555010203")));
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void throwsExceptionWhenActiveOrganizationIsNotSetDuringIssueCreation() {
        authenticationToken.setActiveOrganization(null);

        Issue model = new Issue();
        issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, "title", "comment");
    }

    @Test
    public void updatesIssueStatusWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatus.CLOSED, null);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).update(eq(EXISTING_ISSUE_ID), commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getStatus(), is(equalTo(IssueStatus.CLOSED.name())));
    }

    @Test
    public void createsClosingEventWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatus.CLOSED, null);

        ArgumentCaptor<IssueEvent> issueEventArgumentCaptor = ArgumentCaptor.forClass(IssueEvent.class);
        verify(issueEventService).createEvent(eq(EXISTING_ISSUE_ID), issueEventArgumentCaptor.capture());

        IssueEvent issueEvent = issueEventArgumentCaptor.getValue();
        assertThat(issueEvent.getAuthorName(), is(equalTo("Jane Doe")));
        assertThat(issueEvent.getAuthorPersonalCode(), is(equalTo("EE40102031234")));
        assertThat(issueEvent.getOrganizationName(), is(equalTo("Acme org")));
        assertThat(issueEvent.getOrganizationCode(), is(equalTo("555010203")));
    }

    @Test
    public void createsClosingIssueCommentWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatus.CLOSED, "closing comment");

        ArgumentCaptor<String> issueCommentArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(issueCommentService).createIssueComment(eq(EXISTING_ISSUE_ID), issueCommentArgumentCaptor.capture());

        assertThat(issueCommentArgumentCaptor.getValue(), is(equalTo("closing comment")));
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void throwsExceptionWhenUpdatingClosedIssue() {
        existingIssueEntity.setStatus(IssueStatus.CLOSED.name());

        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatus.CLOSED, null);
    }

    @Test(expected = IllegalBrowserStateException.class)
    public void throwsExceptionWhenActiveOrganizationIsNotSetDuringIssueUpdate() {
        authenticationToken.setActiveOrganization(null);

        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatus.CLOSED, null);
    }

}