package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.CommentRepository;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.service.util.*;
import ee.ria.riha.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static ee.ria.riha.domain.model.IssueStatus.CLOSED;
import static ee.ria.riha.domain.model.IssueStatus.OPEN;
import static ee.ria.riha.domain.model.IssueType.*;
import static ee.ria.riha.service.IssueCommentService.COMMENT_TO_DASHBOARD_ISSUE_COMMENT;
import static ee.ria.riha.service.IssueEventService.COMMENT_TO_ISSUE_EVENT_SUMMARY_MODEL;
import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;
import static ee.ria.riha.service.auth.RoleType.APPROVER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.StringUtils.hasText;

/**
 * Info system issue service
 *
 * @author Valentin Suhnjov
 */
@Service
public class IssueService {

    public static final int WORK_DAYS_UNTIL_DEADLINE = 20;

    public static final Function<Comment, Issue> COMMENT_TO_ISSUE = comment -> {
        if (comment == null) {
            return null;
        }

        return Issue.builder()
                .id(comment.getComment_id())
                .infoSystemUuid(comment.getInfosystem_uuid())
                .dateCreated(comment.getCreation_date())
                .title(comment.getTitle())
                .comment(comment.getComment())
                .authorName(comment.getAuthor_name())
                .authorPersonalCode(comment.getAuthor_personal_code())
                .organizationName(comment.getOrganization_name())
                .organizationCode(comment.getOrganization_code())
                .status(comment.getStatus() != null ? IssueStatus.valueOf(comment.getStatus()) : null)
                .type(comment.getSub_type() != null ? IssueType.valueOf(comment.getSub_type()) : null)
                .resolutionType(comment.getResolution_type() != null
                        ? IssueResolutionType.valueOf(comment.getResolution_type())
                        : null)
                .decisionDeadline(comment.getStatus() == null || IssueStatus.valueOf(comment.getStatus()) == IssueStatus.OPEN
                        ? DateUtils.getDecisionDeadline(comment.getCreation_date(), WORK_DAYS_UNTIL_DEADLINE)
                        : null)
                .build();
    };


    public static final Function<Issue, Comment> ISSUE_TO_COMMENT = issue -> {
        if (issue == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setType(IssueEntityType.ISSUE.name());
        comment.setComment_id(issue.getId());
        comment.setInfosystem_uuid(issue.getInfoSystemUuid());
        comment.setTitle(issue.getTitle());
        comment.setComment(issue.getComment());
        comment.setAuthor_name(issue.getAuthorName());
        comment.setAuthor_personal_code(issue.getAuthorPersonalCode());
        comment.setOrganization_name(issue.getOrganizationName());
        comment.setOrganization_code(issue.getOrganizationCode());
        if (issue.getStatus() != null) {
            comment.setStatus(issue.getStatus().name());
        }
        if (issue.getType() != null) {
            comment.setSub_type(issue.getType().name());
        }
        if (issue.getResolutionType() != null) {
            comment.setResolution_type(issue.getResolutionType().name());
        }

        return comment;
    };

    private static final Function<Comment, RihaIssueSummary> COMMENT_TO_RIHA_ISSUE_SUMMARY = comment -> {
        if (comment == null) {
            return null;
        }

        return RihaIssueSummary.builder()
                .id(comment.getComment_id())
                .infoSystemShortName(comment.getInfosystem_short_name())
                .dateCreated(comment.getCreation_date())
                .title(comment.getTitle())
                .issueType(comment.getSub_type() != null ? IssueType.valueOf(comment.getSub_type()) : null)
                .organizationName(comment.getOrganization_name())
                .organizationCode(comment.getOrganization_code())
                .status(comment.getStatus() != null ? IssueStatus.valueOf(comment.getStatus()) : null)
                .infoSystemFullName(comment.getInfosystem_full_name())
                .resolutionType(comment.getResolution_type() != null
                        ? IssueResolutionType.valueOf(comment.getResolution_type())
                        : null)
                .events(comment.getEvents() == null
                        ? null
                        : comment.getEvents().stream()
                        .map(COMMENT_TO_ISSUE_EVENT_SUMMARY_MODEL)
                        .collect(toList()))
                .decisionDeadline(comment.getStatus() == null || IssueStatus.valueOf(comment.getStatus()) == IssueStatus.OPEN
                        ? DateUtils.getDecisionDeadline(comment.getCreation_date(), WORK_DAYS_UNTIL_DEADLINE)
                        : null)
                .build();
    };

    private static final Function<Comment, DashboardIssue> COMMENT_TO_DASHBOARD_ISSUE = comment -> {
        if (comment == null) {
            return null;
        }

        return DashboardIssue.builder()
                .id(comment.getComment_id())
                .dateCreated(comment.getCreation_date())
                .type(comment.getSub_type() != null ? IssueType.valueOf(comment.getSub_type()) : null)
                .title(comment.getTitle())
                .infoSystemFullName(comment.getInfosystem_full_name())
                .infoSystemShortName(comment.getInfosystem_short_name())
                .authorName(comment.getAuthor_name())
                .organizationName(comment.getOrganization_name())
                .lastComment(comment.getLast_comment_id() != null
                        ? COMMENT_TO_DASHBOARD_ISSUE_COMMENT.apply(comment)
                        : null)
                .build();
    };

    private static final List<IssueType> FEEDBACK_REQUEST_ISSUE_TYPES = Arrays.asList(
            ESTABLISHMENT_REQUEST,
            TAKE_INTO_USE_REQUEST,
            MODIFICATION_REQUEST,
            FINALIZATION_REQUEST);

    private static final List<String> TOPICS_THAT_CANNOT_OPEN_FEEDBACK_REQUEST = Arrays.asList(
            "x-tee alamsüsteem",
            "asutusesiseseks kasutamiseks",
            "dokumendihaldussüsteem");

    private static final String LATEST_INTERACTION_SORT_PARAM = "latest_interaction";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InfoSystemService infoSystemService;

    @Autowired
    private IssueEventService issueEventService;

    @Autowired
    private IssueCommentService issueCommentService;

    @Autowired
    private NotificationService notificationService;


    /**
     * List concrete info system issues.
     *
     * @param reference  info system reference
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of issues
     */
    public PagedResponse<Issue> listInfoSystemIssues(String reference, Pageable pageable, Filterable filterable) {
        InfoSystem infoSystem = infoSystemService.get(reference);

        Filterable filter = new FilterRequest(filterable.getFilter(), filterable.getSort(), filterable.getFields())
                .addFilter(getIssueTypeFilter())
                .addFilter(getInfoSystemUuidEqFilter(infoSystem.getUuid()));

        PagedResponse<Comment> response = commentRepository.list(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                response.getTotalElements(),
                response.getContent().stream()
                        .map(COMMENT_TO_ISSUE)
                        .collect(toList()));
    }

    private String getIssueTypeFilter() {
        return "type,=," + IssueEntityType.ISSUE.name();
    }

    private String getInfoSystemUuidEqFilter(UUID infoSystemUuid) {
        return "infosystem_uuid,=," + infoSystemUuid.toString();
    }

    /**
     * List all RIHA issues.
     *
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated and filtered list of all RIHA issues
     */
    public PagedResponse<RihaIssueSummary> listIssues(Pageable pageable, Filterable filterable) {
        Filterable filter = new FilterRequest(filterable.getFilter(), filterable.getSort(), filterable.getFields());
        PagedResponse<Comment> response = commentRepository.listIssues(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                response.getTotalElements(),
                response.getContent().stream()
                        .map(COMMENT_TO_RIHA_ISSUE_SUMMARY)
                        .collect(toList()));
    }

    /**
     * Creates issue for info system referenced by either UUID or short name
     *
     * @param reference info system reference
     * @param model     issue model
     * @return create issue
     */
    public Issue createInfoSystemIssue(String reference, Issue model) {
        InfoSystem infoSystem = infoSystemService.get(reference);

        validateCreatedIssueType(model);
        validateFeedbackRequestCanBeOpened(infoSystem);
        validateThereIsNoOpenFeedbackRequestIssueOfTheSameType(model, infoSystem.getUuid());

        Issue issue = prepareIssue(model);
        issue.setInfoSystemUuid(infoSystem.getUuid());

        List<Long> createdIssueIds = commentRepository.add(ISSUE_TO_COMMENT.apply(issue));
        if (createdIssueIds.isEmpty()) {
            throw new IllegalBrowserStateException("Issue was not created");
        }

        Issue createdIssue = getIssueById(createdIssueIds.get(0));

        if  (!isFeedbackRequestIssue(model)){
            notificationService.sendNewIssueToSystemContactsNotification(infoSystem);
        }
        notificationService.sendNewIssueToApproversNotification(createdIssue, infoSystem);

        return createdIssue;
    }

    private void validateFeedbackRequestCanBeOpened(InfoSystem infoSystem) {

        if (infoSystem.getTopics().stream()
                .filter(Objects::nonNull)
                .anyMatch(topic -> TOPICS_THAT_CANNOT_OPEN_FEEDBACK_REQUEST.contains(topic.trim().toLowerCase()))) {
            throw new ValidationException("validation.issue.create.feedbackNotNeeded");
        }
    }

    private void validateCreatedIssueType(Issue model) {
        if (model.getType() == null) {
            return;
        }

        if (isFeedbackRequestIssue(model) && !SecurityContextUtil.hasRole(PRODUCER)) {
            throw new ValidationException("validation.issue.create.typeNotAllowed", model.getType());
        }
    }

    private void validateThereIsNoOpenFeedbackRequestIssueOfTheSameType(Issue model, UUID infoSystemUuid) {
        if (!isFeedbackRequestIssue(model)) {
            return;
        }

        FilterRequest request = new FilterRequest();
        request.addFilter(getIssueTypeFilter());
        request.addFilter(getInfoSystemUuidEqFilter(infoSystemUuid));
        request.addFilter("status,=," + IssueStatus.OPEN.name());
        request.addFilter(getIssueSubTypeFilter(model.getType()));

        List<Comment> foundIssues = commentRepository.find(request);

        if (!foundIssues.isEmpty()) {
            throw new ValidationException("validation.issue.create.thereIsOpenFeedbackIssueOfTheSameTypeAlready");
        }
    }

    private Issue prepareIssue(Issue model) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails()
                .orElseThrow(() -> new IllegalBrowserStateException("User details not present in security context"));
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));

        return Issue.builder()
                .title(model.getTitle())
                .comment(model.getComment())
                .authorName(rihaUserDetails.getFullName())
                .authorPersonalCode(rihaUserDetails.getPersonalCode())
                .organizationCode(organization.getCode())
                .organizationName(organization.getName())
                .type(model.getType())
                .status(OPEN)
                .build();
    }

    /**
     * Retrieves single issue by id
     *
     * @param issueId id of an issue
     * @return single issue
     */
    public Issue getIssueById(Long issueId) {
        Comment issue = commentRepository.get(issueId);

        if (IssueEntityType.valueOf(issue.getType()) != IssueEntityType.ISSUE) {
            throw new IllegalBrowserStateException("Retrieved entity is not an issue");
        }

        return COMMENT_TO_ISSUE.apply(issue);
    }

    private boolean isFeedbackRequestIssue(Issue issue) {
        return FEEDBACK_REQUEST_ISSUE_TYPES.contains(issue.getType());
    }

    private String getIssueSubTypeFilter(IssueType issueType) {
        return "sub_type,=," + issueType.name();
    }

    /**
     * Updates issue status. Throws exception in case current status is not {@link IssueStatus#OPEN}.
     *
     * @param issueId id of an issue
     * @param model   model of an issue
     * @return updated issue
     */
    public Issue updateIssueStatus(Long issueId, IssueStatusUpdateModel model) {
        return updateIssueStatus(getIssueById(issueId), model);
    }

    /**
     * Updates issue status. Throws exception in case current status is not {@link IssueStatus#OPEN}.
     *
     * @param issue updated issue
     * @param model model of an issue
     * @return updated issue
     */
    public Issue updateIssueStatus(Issue issue, IssueStatusUpdateModel model) {
        validateIssueNotClosed(issue);
        validateUpdatedIssueStatus(issue, model);
        validateUpdatedFeedbackRequestIssueResolution(issue, model);

        String comment = model.getComment();
        boolean commented = hasText(comment);
        if (commented) {
            issueCommentService.createIssueCommentWithoutNotification(issue.getId(), IssueCommentModel.builder()
                    .comment(comment)
                    .build());
        }

        if (model.getStatus() == CLOSED) {
            closeIssue(issue, model);
        }

        Issue updatedIssue = getIssueById(issue.getId());
        notificationService.sendIssueStatusUpdateNotification(updatedIssue, commented);

        return updatedIssue;
    }

    private void validateIssueNotClosed(Issue issue) {
        if (issue.getStatus() == CLOSED) {
            throw new ValidationException("validation.issue.update.alreadyClosed");
        }
    }

    private void validateUpdatedIssueStatus(Issue issue, IssueStatusUpdateModel model) {
        if (issue.getStatus() == model.getStatus()) {
            throw new ValidationException("validation.issue.update.statusDidNotChange");
        }
    }

    private void validateUpdatedFeedbackRequestIssueResolution(Issue issue, IssueStatusUpdateModel model) {
        if (!isFeedbackRequestIssue(issue)) {
            return;
        }

        if (!SecurityContextUtil.isRiaApprover()) {
            throw new ValidationException("validation.issue.update.noRightToCloseFeedbackIssue");
        }

        if (model.getResolutionType() == null) {
            throw new ValidationException("validation.issue.update.noResolutionForFeedbackIssue");
        }

        if (model.getResolutionType() != IssueResolutionType.POSITIVE && model.getResolutionType() != IssueResolutionType.NEGATIVE) {
            throw new ValidationException("validation.issue.update.unacceptableResolutionTypeForFeedbackIssue");
        }
    }

    private void closeIssue(Issue issue, IssueStatusUpdateModel model) {
        IssueResolutionType resolutionType = isFeedbackRequestIssue(issue)
                ? model.getResolutionType()
                : null;
        createCloseEvent(issue.getId(), resolutionType, model.getComment());

        issue.setStatus(CLOSED);
        issue.setResolutionType(resolutionType);
        commentRepository.update(issue.getId(), ISSUE_TO_COMMENT.apply(issue));
    }

    private void createCloseEvent(Long issueId, IssueResolutionType resolutionType, String comment) {
        IssueEvent closeEvent = prepareIssueEvent(IssueEventType.CLOSED, comment);
        closeEvent.setResolutionType(resolutionType);

        issueEventService.createEvent(issueId, closeEvent);
    }

    private IssueEvent prepareIssueEvent(IssueEventType eventType, @Nullable String comment) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails()
                .orElseThrow(() -> new IllegalBrowserStateException("User details not present in security context"));
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));

        return IssueEvent.builder()
                .type(eventType)
                .authorName(rihaUserDetails.getFullName())
                .authorPersonalCode(rihaUserDetails.getPersonalCode())
                .organizationName(organization.getName())
                .organizationCode(organization.getCode())
                .comment(comment)
                .build();
    }

    /**
     * Retrieves set of personal codes including issue author and every commenter.
     *
     * @param issueId id of an issue
     * @return set of personal codes
     */
    public Set<String> getParticipantsPersonalCodes(Long issueId) {
        Set<String> issueCommentsAuthorsPersonalCodes = issueCommentService.listByIssueId(issueId).stream()
                .map(IssueComment::getAuthorPersonalCode)
                .collect(toSet());

        String issueAuthorPersonalCode = getIssueById(issueId).getAuthorPersonalCode();
        issueCommentsAuthorsPersonalCodes.add(issueAuthorPersonalCode);

        return issueCommentsAuthorsPersonalCodes;
    }

    /**
     * Creates comment and event that describe issue approval decision.
     *
     * @param issueId id of an issue
     * @param model   decision model
     */
    public void makeApprovalDecision(Long issueId, IssueApprovalDecisionModel model) {
        makeApprovalDecision(getIssueById(issueId), model);
    }

    /**
     * Creates comment and event that describe issue approval decision.
     *
     * @param issue an issue for which decision is made
     * @param model decision model
     */
    public void makeApprovalDecision(Issue issue, IssueApprovalDecisionModel model) {
        validateIssueNotClosed(issue);
        validateApprovalDecision(issue, model);

        if (hasText(model.getComment())) {
            issueCommentService.createIssueCommentWithoutNotification(issue.getId(), IssueCommentModel.builder()
                    .comment(model.getComment())
                    .build());
        }

        createDecisionEvent(issue.getId(), model.getDecisionType(), model.getComment());
    }

    private void validateApprovalDecision(Issue issue, IssueApprovalDecisionModel model) {
        if (!isFeedbackRequestIssue(issue)) {
            throw new ValidationException("validation.issueApprovalDecision.create.notAFeedbackIssue");
        }

        if (model.getDecisionType() == null) {
            throw new ValidationException("validation.issueApprovalDecision.create.noDecisionTypeSpecified");
        }

        if (!SecurityContextUtil.hasRole(APPROVER)) {
            throw new ValidationException("validation.issueApprovalDecision.create.noRightToAddDecision");
        }
    }

    private void createDecisionEvent(Long issueId, IssueResolutionType decisionType, String comment) {
        IssueEvent decisionEvent = prepareIssueEvent(IssueEventType.DECISION, comment);
        decisionEvent.setResolutionType(decisionType);

        issueEventService.createEvent(issueId, decisionEvent);
        notificationService.sendNewIssueDecisionNotification(decisionEvent);
    }

    public PagedResponse<DashboardIssue> listOrganizationInfosystemIssues(String organizationCode,
                                                                          CompositeFilterRequest filter,
                                                                          Pageable pageable) {
        filter.addFilterParameter(getOrganizationInfoSystemIssuesCompositeFilter(organizationCode));
        return listDashboardIssues(pageable, filter);
    }

    private String getOrganizationInfoSystemIssuesCompositeFilter(String organizationCode) {
        return "organization-infosystems-issues:" + organizationCode;
    }

    public PagedResponse<DashboardIssue> listDashboardIssues(Pageable pageable, CompositeFilterRequest filter) {
        boolean latestInteractionSort = filter.getSortParameters().contains(LATEST_INTERACTION_SORT_PARAM);
        if (latestInteractionSort) {
            filter = new CompositeFilterRequest(filter.getFilterParameters(), null);
        }

        PagedGridResponse<Comment> response = commentRepository.listDashboardIssues(pageable, filter);
        Stream<DashboardIssue> issueStream = response.getContent().stream()
                        .map(COMMENT_TO_DASHBOARD_ISSUE);

        // Exceptional sorting logic to distinguish most recently created/commented issues
        // Created within RIHAKB-742 due to insufficient time for broader implementation
        // TODO move sorting logic to Storage backend when compatible
        if (latestInteractionSort) {
            issueStream = issueStream.sorted(Comparator.comparing(this::latestInteraction).reversed());
        }

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                response.getTotalElements(),
                issueStream.collect(toList()));
    }

    private Date latestInteraction(DashboardIssue issue) {
        Date dateCreated = issue.getDateCreated();
        DashboardIssueComment lastComment = issue.getLastComment();
        return lastComment == null || dateCreated.after(lastComment.getDateCreated()) ? dateCreated : lastComment.getDateCreated();
    }
}
