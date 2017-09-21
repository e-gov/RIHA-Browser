package ee.ria.riha.service;

import ee.ria.riha.domain.model.*;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import ee.ria.riha.storage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Function;

import static ee.ria.riha.domain.model.IssueStatus.CLOSED;
import static ee.ria.riha.domain.model.IssueStatus.OPEN;
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
        comment.setType(EntityType.ISSUE.name());
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

    /**
     * List issues of all info systems.
     *
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of issues
     */
    public PagedResponse<Issue> listIssues(Pageable pageable, Filterable filterable) {
        Filterable filter = new FilterRequest(filterable.getFilter(), filterable.getSort(), filterable.getFields())
                .addFilter(getIssueTypeFilter());

        PagedResponse<Comment> response = commentRepository.list(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                                   response.getTotalElements(),
                                   response.getContent().stream()
                                           .map(COMMENT_TO_ISSUE)
                                           .collect(toList()));
    }

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

        if (EntityType.valueOf(issue.getType()) != EntityType.ISSUE) {
            throw new IllegalBrowserStateException("Retrieved entity is not an issue");
        }

        return COMMENT_TO_ISSUE.apply(issue);
    }

    /**
     * Creates issue for info system with a given short name
     *
     * @param shortName info system short name
     * @param issue     issue model
     * @return create issue
     */
    public Issue createInfoSystemIssue(String shortName, Issue issue) {
        InfoSystem infoSystem = infoSystemService.get(shortName);

        issue.setInfoSystemUuid(infoSystem.getUuid());
        issue.setStatus(OPEN);

        Long issueId = commentRepository.add(ISSUE_TO_COMMENT.apply(issue)).get(0);

        return COMMENT_TO_ISSUE.apply(commentRepository.get(issueId));
    }

    /**
     * Updates issue status. Throws exception in case current status is not {@link IssueStatus#OPEN}.
     *
     * @param issueId id of an issue
     * @param model   updated issue model
     * @return updated issue
     */
    public Issue updateIssue(Long issueId, Issue model) {
        Issue issue = getIssueById(issueId);

        if (issue.getStatus() == CLOSED) {
            throw new IllegalBrowserStateException("Can't modify closed issue");
        }

        if (StringUtils.hasText(model.getComment())) {
            IssueComment issueComment = IssueComment.builder()
                    .comment(model.getComment())
                    .authorName(model.getAuthorName())
                    .authorPersonalCode(model.getAuthorPersonalCode())
                    .organizationName(model.getOrganizationName())
                    .organizationCode(model.getOrganizationCode())
                    .build();

            issueCommentService.createIssueComment(issueId, issueComment);
        }

        if (issue.getStatus() != model.getStatus()) {
            IssueEvent issueClosedEvent = IssueEvent.builder()
                    .type(IssueEventType.CLOSED)
                    .authorName(model.getAuthorName())
                    .authorPersonalCode(model.getAuthorPersonalCode())
                    .organizationName(model.getOrganizationName())
                    .organizationCode(model.getOrganizationCode())
                    .build();

            issueEventService.createEvent(issueId, issueClosedEvent);

            issue.setStatus(model.getStatus());
        }

        commentRepository.update(issueId, ISSUE_TO_COMMENT.apply(issue));
        return getIssueById(issueId);
    }

    private String getIssueTypeFilter() {
        return "type,=," + EntityType.ISSUE.name();
    }

    private String getInfoSystemUuidEqFilter(UUID infoSystemUuid) {
        return "infosystem_uuid,=," + infoSystemUuid.toString();
    }

}
