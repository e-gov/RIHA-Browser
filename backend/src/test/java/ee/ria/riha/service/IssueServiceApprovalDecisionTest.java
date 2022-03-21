package ee.ria.riha.service;

import ee.ria.riha.TestUtils;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.web.model.IssueApprovalDecisionModel;
import ee.ria.riha.web.model.IssueCommentModel;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class IssueServiceApprovalDecisionTest {

    private static final Long EXISTING_ISSUE_ID = 15503L;

    private static final String ACME_REG_CODE = "555010203";
    private static final String EVS_REG_CODE = "70001234";

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    @Mock
    private IssueEventService issueEventService;

    @Mock
    private IssueCommentService issueCommentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private IssueService issueService;

    private Issue existingIssue = Issue.builder()
            .id(EXISTING_ISSUE_ID)
            .status(IssueStatus.OPEN)
            .type(IssueType.MODIFICATION_REQUEST)
            .build();

    private Authentication authenticationToken = TestUtils.getOAuth2LoginToken(null, null);

    @Before
    public void setUp() {
        // Reset authorization
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        TestUtils.setActiveOrganisation(authenticationToken, null);
    }


    @Test
    public void createsApprovalDecisionCommentAndEvent() {
        setApproverRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .comment("Decision comment")
                .decisionType(IssueResolutionType.DISMISSED)
                .build());

        ArgumentCaptor<IssueCommentModel> issueCommentArgumentCaptor = ArgumentCaptor.forClass(IssueCommentModel.class);
        verify(issueCommentService).createIssueCommentWithoutNotification(eq(EXISTING_ISSUE_ID),
                issueCommentArgumentCaptor.capture());

        IssueCommentModel issueComment = issueCommentArgumentCaptor.getValue();
        assertThat(issueComment.getComment(), is(equalTo("Decision comment")));

        ArgumentCaptor<IssueEvent> issueEventArgumentCaptor = ArgumentCaptor.forClass(IssueEvent.class);
        verify(issueEventService).createEvent(eq(EXISTING_ISSUE_ID), issueEventArgumentCaptor.capture());

        IssueEvent issueEvent = issueEventArgumentCaptor.getValue();
        assertThat(issueEvent.getType(), is(Matchers.equalTo(IssueEventType.DECISION)));
        assertThat(issueEvent.getResolutionType(), is(Matchers.equalTo(IssueResolutionType.DISMISSED)));
        assertThat(issueEvent.getAuthorName(), is(Matchers.equalTo("Jane Doe")));
        assertThat(issueEvent.getAuthorPersonalCode(), is(Matchers.equalTo("EE40102031234")));
        assertThat(issueEvent.getOrganizationName(), is(Matchers.equalTo("Eesti Väikeloomaarstide Selts")));
        assertThat(issueEvent.getOrganizationCode(), is(Matchers.equalTo("70001234")));
    }

    private void setApproverRole() {
        TestUtils.setActiveOrganisation(authenticationToken, EVS_REG_CODE);
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionWhenProducerTriesToMakeDecision() {
        setProducerRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .decisionType(IssueResolutionType.DISMISSED)
                .build());
    }

    private void setProducerRole() {
        TestUtils.setActiveOrganisation(authenticationToken, ACME_REG_CODE);
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionWhenUserWithoutRightsTriesToMakeDecision() {
        setAuthenticatedUserRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .decisionType(IssueResolutionType.DISMISSED)
                .build());
    }

    private void setAuthenticatedUserRole() {
        TestUtils.setActiveOrganisation(authenticationToken, null);
    }

    @Test
    public void doesNotAddCommentWhenItWasNotProvided() {
        setApproverRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .comment("")
                .decisionType(IssueResolutionType.POSITIVE)
                .build());

        verify(issueCommentService, never()).createIssueCommentWithoutNotification(anyLong(),
                any(IssueCommentModel.class));
        verify(issueCommentService, never()).createIssueComment(anyLong(),
                any(IssueCommentModel.class));
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionForNonFeedbackRequestIssue() {
        setApproverRole();

        existingIssue.setType(null);

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .comment("should not happen")
                .decisionType(IssueResolutionType.POSITIVE)
                .build());
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionWhenDecisionWasNotProvided() {
        setApproverRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .comment("decision must be provided")
                .build());
    }

}
