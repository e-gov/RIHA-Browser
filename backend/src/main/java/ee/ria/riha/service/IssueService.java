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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ee.ria.riha.domain.model.IssueStatus.CLOSED;
import static ee.ria.riha.domain.model.IssueStatus.OPEN;
import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;
import static java.util.stream.Collectors.toList;

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

        return comment;
    };

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
     * Creates issue for info system with a given short name
     *
     * @param shortName info system short name
     * @param title     issue title
     * @param comment   issue comment
     * @return create issue
     */
    public Issue createInfoSystemIssue(String shortName, String title, String comment) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        if (rihaUserDetails == null) {
            throw new IllegalBrowserStateException("User details not present in security context");
        }

        RihaOrganization organization = getActiveOrganization();
        if (organization == null) {
            throw new ValidationException("validation.generic.activeOrganization.notSet");
        }

        InfoSystem infoSystem = infoSystemService.get(shortName);

        Issue issue = Issue.builder()
                .infoSystemUuid(infoSystem.getUuid())
                .title(title)
                .comment(comment)
                .authorName(rihaUserDetails.getFullName())
                .authorPersonalCode(rihaUserDetails.getPersonalCode())
                .organizationCode(organization.getCode())
                .organizationName(organization.getName())
                .status(OPEN)
                .build();

        List<Long> createIssueIds = commentRepository.add(ISSUE_TO_COMMENT.apply(issue));
        if (createIssueIds.isEmpty()) {
            throw new IllegalBrowserStateException("Issue was not created");
        }

        notificationService.sendNewIssueNotification(infoSystem);

        return COMMENT_TO_ISSUE.apply(commentRepository.get(createIssueIds.get(0)));
    }

    /**
     * Updates issue status. Throws exception in case current status is not {@link IssueStatus#OPEN}.
     *
     * @param issueId   id of an issue
     * @param newStatus updated issue status
     * @param comment   @return updated issue
     */
    public Issue updateIssueStatus(Long issueId, IssueStatus newStatus, String comment) {
        Issue issue = getIssueById(issueId);

        if (issue.getStatus() == CLOSED) {
            throw new IllegalBrowserStateException("Can't modify closed issue");
        }

        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        if (rihaUserDetails == null) {
            throw new IllegalBrowserStateException("User details not present in security context");
        }

        RihaOrganization organization = getActiveOrganization();
        if (organization == null) {
            throw new ValidationException("validation.generic.activeOrganization.notSet");
        }

        if (StringUtils.hasText(comment)) {
            issueCommentService.createIssueComment(issueId, comment);
        }

        if (issue.getStatus() != newStatus) {
            IssueEvent issueClosedEvent = IssueEvent.builder()
                    .type(IssueEventType.CLOSED)
                    .authorName(rihaUserDetails.getFullName())
                    .authorPersonalCode(rihaUserDetails.getPersonalCode())
                    .organizationName(organization.getName())
                    .organizationCode(organization.getCode())
                    .build();

            issueEventService.createEvent(issueId, issueClosedEvent);

            issue.setStatus(newStatus);
        }

        commentRepository.update(issueId, ISSUE_TO_COMMENT.apply(issue));
        return getIssueById(issueId);
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
                .collect(Collectors.toSet());

        String issueAuthorPersonalCode = getIssueById(issueId).getAuthorPersonalCode();
        issueCommentsAuthorsPersonalCodes.add(issueAuthorPersonalCode);

        return issueCommentsAuthorsPersonalCodes;
    }

    private String getIssueTypeFilter() {
        return "type,=," + IssueEntityType.ISSUE.name();
    }

    private String getInfoSystemUuidEqFilter(UUID infoSystemUuid) {
        return "infosystem_uuid,=," + infoSystemUuid.toString();
    }

}
