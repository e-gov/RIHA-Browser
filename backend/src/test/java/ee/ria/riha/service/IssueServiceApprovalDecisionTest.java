package ee.ria.riha.service;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static ee.ria.riha.service.auth.RoleType.APPROVER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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

    @InjectMocks
    private IssueService issueService;

    private Issue existingIssue = Issue.builder()
            .id(EXISTING_ISSUE_ID)
            .status(IssueStatus.OPEN)
            .type(IssueType.MODIFICATION_REQUEST)
            .build();

    private RihaOrganizationAwareAuthenticationToken authenticationToken =
            JaneAuthenticationTokenBuilder.builder()
                    .setOrganizations(ImmutableMultimap.of(
                            new RihaOrganization(ACME_REG_CODE, "Acme org"),
                            new SimpleGrantedAuthority(PRODUCER.getRole()),
                            new RihaOrganization(EVS_REG_CODE, "Eesti Väikeloomaarstide Selts"),
                            new SimpleGrantedAuthority(APPROVER.getRole())))
                    .build();

    @Before
    public void setUp() {
        // Reset authorization
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        authenticationToken.setActiveOrganization(null);
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
        authenticationToken.setActiveOrganization(EVS_REG_CODE);
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionWhenProducerTriesToMakeDecision() {
        setProducerRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .decisionType(IssueResolutionType.DISMISSED)
                .build());
    }

    private void setProducerRole() {
        authenticationToken.setActiveOrganization(ACME_REG_CODE);
    }

    @Test(expected = ValidationException.class)
    public void throwsExceptionWhenUserWithoutRightsTriesToMakeDecision() {
        setAuthenticatedUserRole();

        issueService.makeApprovalDecision(existingIssue, IssueApprovalDecisionModel.builder()
                .decisionType(IssueResolutionType.DISMISSED)
                .build());
    }

    private void setAuthenticatedUserRole() {
        authenticationToken.setActiveOrganization(null);
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
