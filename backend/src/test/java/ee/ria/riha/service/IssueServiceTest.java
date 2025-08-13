package ee.ria.riha.service;

import ee.ria.riha.TestUtils;
import ee.ria.riha.domain.CommentRepository;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.web.model.IssueCommentModel;
import ee.ria.riha.web.model.IssueStatusUpdateModel;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static ee.ria.riha.domain.model.IssueType.ESTABLISHMENT_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    private static final String EXISTING_INFO_SYSTEM_SHORT_NAME = "is1";
    private static final Long EXISTING_ISSUE_ID = 15503L;

    private static final String ACME_REG_CODE = "555010203";
    private static final String EVS_REG_CODE = "70001234";
    private static final String RIA_REG_CODE = "70006317";
    private static final UUID EXISTING_INFO_SYSTEM_UUID = UUID.fromString("01234567-0123-0123-0123-0123456789ab");

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    private final Authentication authenticationToken = TestUtils.getOAuth2LoginToken(null, null);

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

    @Mock
    private RelationService relationService;

    @InjectMocks
    private IssueService issueService;

    private final InfoSystem existingInfoSystem = new InfoSystem();

    private final Issue existingIssue = Issue.builder()
            .id(EXISTING_ISSUE_ID)
            .status(IssueStatus.OPEN)
            .build();

    private final Comment existingIssueEntity = IssueService.ISSUE_TO_COMMENT.apply(existingIssue);

    private final Map<Long, Comment> createdIssues = new HashMap<>();

    @BeforeEach
    public void setUp() {
        // Reset authorization
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        setProducerRole();

        existingInfoSystem.setShortName(EXISTING_INFO_SYSTEM_SHORT_NAME);
        existingInfoSystem.setUuid(EXISTING_INFO_SYSTEM_UUID);

        when(infoSystemService.get(EXISTING_INFO_SYSTEM_SHORT_NAME)).thenReturn(existingInfoSystem);

        createdIssues.put(EXISTING_ISSUE_ID, existingIssueEntity);

        when(commentRepository.get(any(Long.class))).thenAnswer((Answer<Comment>) invocation -> {
            Long commentId = invocation.getArgument(0);
            return createdIssues.get(commentId);
        }

        );
        when(commentRepository.add(any(Comment.class))).thenAnswer((Answer<List<Long>>) invocation -> {
            Comment createdComment = invocation.getArgument(0);
            createdComment.setComment_id(42L);
            createdIssues.put(createdComment.getComment_id(), createdComment);
            return List.of(createdComment.getComment_id());
        });

        doNothing().when(notificationService).sendNewIssueToSystemContactsNotification(any(InfoSystem.class));
        doNothing().when(notificationService).sendNewIssueToApproversNotification(any(Issue.class),
                any(InfoSystem.class));
    }

    private void setProducerRole() {
        TestUtils.setActiveOrganisation(authenticationToken, ACME_REG_CODE);
    }

    @Test
    public void createsIssueWithTitleAndComment() {
        issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()

                .title("critical issue")
                .comment("clear problem description")
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).add(commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getTitle(), is(equalTo("critical issue")));
        assertThat(comment.getComment(), is(equalTo("clear problem description")));
    }

    @Test
    public void populatesAuthorAndOrganizationFromUserDetailsDuringIssueCreation() {
        issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()
                .title("title")
                .comment("comment")
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).add(commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getAuthor_name(), is(equalTo("Jane Doe")));
        assertThat(comment.getAuthor_personal_code(), is(equalTo("EE40102031234")));
        assertThat(comment.getOrganization_name(), is(equalTo("Acme org")));
        assertThat(comment.getOrganization_code(), is(equalTo("555010203")));
    }

    @Test
    public void throwsExceptionWhenActiveOrganizationIsNotSetDuringIssueCreation() {
        assertThrows(IllegalBrowserStateException.class, () -> {
            TestUtils.setActiveOrganisation(authenticationToken, null);

            issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()
                    .title("title")
                    .comment("comment")
                    .build());
        });
    }

    @Test
    public void throwsExceptionWhenApproverTriesToCreateRequestIssue() {
        assertThrows(ValidationException.class, () -> {
            setNonRiaApproverRole();

            issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()
                    .title("title")
                    .comment("comment")
                    .type(ESTABLISHMENT_REQUEST)
                    .build());
        });
    }

    private void setNonRiaApproverRole() {
        TestUtils.setActiveOrganisation(authenticationToken, (EVS_REG_CODE));
    }

    @Test
    public void setsIssueTypeWhenKirjeldajaCreatesIssue() {
        Issue issue = issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()
                .title("title")
                .comment("comment")
                .type(ESTABLISHMENT_REQUEST)
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).add(commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getSub_type(), is(equalTo(ESTABLISHMENT_REQUEST.name())));
    }

    @Test
    public void updatesIssueStatusWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                .status(IssueStatus.CLOSED)
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).update(eq(EXISTING_ISSUE_ID), commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getStatus(), is(equalTo(IssueStatus.CLOSED.name())));
    }

    @Test
    public void createsClosingEventWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                .status(IssueStatus.CLOSED)
                .build());

        ArgumentCaptor<IssueEvent> issueEventArgumentCaptor = ArgumentCaptor.forClass(IssueEvent.class);
        verify(issueEventService).createEvent(eq(EXISTING_ISSUE_ID), issueEventArgumentCaptor.capture());

        IssueEvent issueEvent = issueEventArgumentCaptor.getValue();
        assertThat(issueEvent.getType(), is(equalTo(IssueEventType.CLOSED)));
        assertThat(issueEvent.getAuthorName(), is(equalTo("Jane Doe")));
        assertThat(issueEvent.getAuthorPersonalCode(), is(equalTo("EE40102031234")));
        assertThat(issueEvent.getOrganizationName(), is(equalTo("Acme org")));
        assertThat(issueEvent.getOrganizationCode(), is(equalTo("555010203")));
    }

    @Test
    public void createsClosingIssueCommentWhenIssueIsClosed() {
        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                .status(IssueStatus.CLOSED)
                .comment("closing comment")
                .build());

        ArgumentCaptor<IssueCommentModel> issueCommentArgumentCaptor = ArgumentCaptor.forClass(IssueCommentModel.class);
        verify(issueCommentService).createIssueCommentWithoutNotification(eq(EXISTING_ISSUE_ID),
                issueCommentArgumentCaptor.capture());

        assertThat(issueCommentArgumentCaptor.getValue().getComment(), is(equalTo("closing comment")));
    }

    @Test
    public void throwsExceptionWhenUpdatingClosedIssue() {
        assertThrows(ValidationException.class, () -> {
            existingIssueEntity.setStatus(IssueStatus.CLOSED.name());

            issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .build());
        });
    }

    @Test
    public void throwsExceptionWhenActiveOrganizationIsNotSetDuringIssueUpdate() {
        assertThrows(IllegalBrowserStateException.class, () -> {
            TestUtils.setActiveOrganisation(authenticationToken, null);

            issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .build());
        });
    }

    @Test
    public void doesNotSetResolutionTypeForNonFeedbackRequestIssue() {
        setNonRiaApproverRole();

        existingIssue.setType(null);

        issueService.updateIssueStatus(EXISTING_ISSUE_ID, IssueStatusUpdateModel.builder()
                .status(IssueStatus.CLOSED)
                .resolutionType(IssueResolutionType.POSITIVE)
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).update(eq(EXISTING_ISSUE_ID), commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getStatus(), is(equalTo(IssueStatus.CLOSED.name())));
        assertThat(comment.getResolution_type(), is(nullValue()));
    }

    @Test
    public void setsResolutionTypeForFeedbackRequestIssue() {
        setRiaApproverRole();

        existingIssue.setType(IssueType.ESTABLISHMENT_REQUEST);

        issueService.updateIssueStatus(existingIssue, IssueStatusUpdateModel.builder()
                .status(IssueStatus.CLOSED)
                .resolutionType(IssueResolutionType.POSITIVE)
                .build());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).update(eq(EXISTING_ISSUE_ID), commentArgumentCaptor.capture());

        Comment comment = commentArgumentCaptor.getValue();
        assertThat(comment.getStatus(), is(equalTo(IssueStatus.CLOSED.name())));
        assertThat(comment.getResolution_type(), is(equalTo(IssueResolutionType.POSITIVE.name())));
    }

    private void setRiaApproverRole() {
        TestUtils.setActiveOrganisation(authenticationToken, RIA_REG_CODE);
    }

    @Test
    public void doesNotAllowNonRiaUsersToCloseFeedbackIssues() {
        assertThrows(ValidationException.class, () -> {
            setNonRiaApproverRole();

            existingIssue.setType(IssueType.ESTABLISHMENT_REQUEST);

            issueService.updateIssueStatus(existingIssue, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .resolutionType(IssueResolutionType.POSITIVE)
                    .build());
        });
    }

    @Test
    public void doesNotAllowFeedbackIssueResolutionWithoutApproverRole() {
        assertThrows(ValidationException.class, () -> {
            setProducerRole();

            existingIssue.setType(IssueType.ESTABLISHMENT_REQUEST);

            issueService.updateIssueStatus(existingIssue, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .resolutionType(IssueResolutionType.POSITIVE)
                    .build());
        });
    }

    @Test
    public void doesNotAllowToCloseFeedbackIssueWithoutResolution() {
        assertThrows(ValidationException.class, () -> {
            setNonRiaApproverRole();

            existingIssue.setType(IssueType.ESTABLISHMENT_REQUEST);

            issueService.updateIssueStatus(existingIssue, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .build());
        });
    }

    @Test
    public void doesNotAllowToResolveIssueWithDismissedResolutionType() {
        assertThrows(ValidationException.class, () -> {
            setRiaApproverRole();

            existingIssue.setType(IssueType.ESTABLISHMENT_REQUEST);

            issueService.updateIssueStatus(existingIssue, IssueStatusUpdateModel.builder()
                    .status(IssueStatus.CLOSED)
                    .resolutionType(IssueResolutionType.DISMISSED)
                    .build());
        });
    }

    @Test
    public void doesNotAllowToCreateFeedbackIssueWhenThereIsOpenIssueOfTheSameType() {
        assertThrows(ValidationException.class, () -> {
            when(commentRepository.find(any())).thenReturn(Collections.singletonList(existingIssueEntity));

            Issue issue = issueService.createInfoSystemIssue(EXISTING_INFO_SYSTEM_SHORT_NAME, Issue.builder()
                    .title("title")
                    .comment("comment")
                    .type(ESTABLISHMENT_REQUEST)
                    .build());
        });
    }
}
