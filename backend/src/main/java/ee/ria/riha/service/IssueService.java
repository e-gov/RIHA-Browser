package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import ee.ria.riha.storage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static ee.ria.riha.domain.model.IssueStatus.CLOSED;
import static ee.ria.riha.domain.model.IssueStatus.OPEN;
import static ee.ria.riha.domain.model.IssueType.*;
import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;
import static ee.ria.riha.service.auth.RoleType.APPROVER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Info system issue service
 *
 * @author Valentin Suhnjov
 */
@Service
public class IssueService {

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
                .organizationName(comment.getOrganization_name())
                .organizationCode(comment.getOrganization_code())
                .status(comment.getStatus() != null ? IssueStatus.valueOf(comment.getStatus()) : null)
                .build();
    };

    private static final List<IssueType> FEEDBACK_REQUEST_ISSUE_TYPES = Arrays.asList(
            ESTABLISHMENT_REQUEST,
            TAKE_INTO_USE_REQUEST,
            MODIFICATION_REQUEST,
            FINALIZATION_REQUEST);

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
     * @param shortName  info system short name
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of issues
     */
    public PagedResponse<Issue> listInfoSystemIssues(String shortName, Pageable pageable, Filterable filterable) {
        InfoSystem infoSystem = infoSystemService.get(shortName);

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

    private String getIssueTypeFilter() {
        return "type,=," + IssueEntityType.ISSUE.name();
    }

    private String getInfoSystemUuidEqFilter(UUID infoSystemUuid) {
        return "infosystem_uuid,=," + infoSystemUuid.toString();
    }

    /**
     * Creates issue for info system with a given short name
     *
     * @param shortName info system short name
     * @param model     issue model
     * @return create issue
     */
    public Issue createInfoSystemIssue(String shortName, Issue model) {
        validateCreatedIssueType(model);

        InfoSystem infoSystem = infoSystemService.get(shortName);

        Issue issue = prepareIssue(model);
        issue.setInfoSystemUuid(infoSystem.getUuid());

        List<Long> createdIssueIds = commentRepository.add(ISSUE_TO_COMMENT.apply(issue));
        if (createdIssueIds.isEmpty()) {
            throw new IllegalBrowserStateException("Issue was not created");
        }

        Issue createdIssue = getIssueById(createdIssueIds.get(0));

        notificationService.sendNewIssueToSystemContactsNotification(infoSystem);
        notificationService.sendNewIssueToApproversNotification(model.getTitle(), infoSystem);

        return createdIssue;
    }

    private void validateCreatedIssueType(Issue model) {
        if (model.getType() == null) {
            return;
        }

        if (FEEDBACK_REQUEST_ISSUE_TYPES.contains(model.getType())
                && !SecurityContextUtil.hasRole(PRODUCER)) {
            throw new ValidationException("validation.issue.create.typeNotAllowed", model.getType());
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

    /**
     * Updates issue status. Throws exception in case current status is not {@link IssueStatus#OPEN}.
     *
     * @param issueId   id of an issue
     * @param newStatus updated issue status
     * @param comment   status update comment
     * @return updated issue
     */
    public Issue updateIssueStatus(Long issueId, IssueStatus newStatus, String comment) {
        Issue issue = getIssueById(issueId);
        validateUpdatedIssueCurrentStatus(issue);
        validateUpdatedIssueNewStatus(issue, newStatus);

        if (StringUtils.hasText(comment)) {
            issueCommentService.createIssueComment(issueId, comment);
        }

        prepareIssueEvent(newStatus).ifPresent(issueEvent -> issueEventService.createEvent(issueId, issueEvent));

        issue.setStatus(newStatus);
        commentRepository.update(issueId, ISSUE_TO_COMMENT.apply(issue));

        return getIssueById(issueId);
    }

    private void validateUpdatedIssueCurrentStatus(Issue issue) {
        if (issue.getStatus() == CLOSED) {
            throw new ValidationException("validation.issue.update.alreadyClosed");
        }
    }

    private void validateUpdatedIssueNewStatus(Issue issue, IssueStatus newStatus) {
        if (issue.getStatus() == newStatus) {
            throw new ValidationException("validation.issue.update.statusDidNotChange");
        }

        if (newStatus == CLOSED
                && FEEDBACK_REQUEST_ISSUE_TYPES.contains(issue.getType())
                && !SecurityContextUtil.hasRole(APPROVER)) {
            throw new ValidationException("validation.issue.update.noRightToCloseFeedbackIssue");
        }
    }

    private Optional<IssueEvent> prepareIssueEvent(IssueStatus newStatus) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails()
                .orElseThrow(() -> new IllegalBrowserStateException("User details not present in security context"));
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));

        if (newStatus.equals(IssueStatus.CLOSED)) {
            IssueEvent issueClosedEvent = IssueEvent.builder()
                    .type(IssueEventType.CLOSED)
                    .authorName(rihaUserDetails.getFullName())
                    .authorPersonalCode(rihaUserDetails.getPersonalCode())
                    .organizationName(organization.getName())
                    .organizationCode(organization.getCode())
                    .build();

            return Optional.of(issueClosedEvent);
        }

		return Optional.empty();
    }

    /**
     * Retrieves set of unique participants personal codes.
     *
     * @param issueId - issue id
     * @return retrieved set of unique participants personal codes
     */
    public Set<String> getParticipantsPersonalCodes(Long issueId) {
        Set<String> issueCommentsAuthorsPersonalCodes = issueCommentService.listByIssueId(issueId).stream()
                .map(IssueComment::getAuthorPersonalCode)
                .collect(toSet());

        String issueAuthorPersonalCode = getIssueById(issueId).getAuthorPersonalCode();
        issueCommentsAuthorsPersonalCodes.add(issueAuthorPersonalCode);

        return issueCommentsAuthorsPersonalCodes;
    }

}
