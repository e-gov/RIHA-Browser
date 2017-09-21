package ee.ria.riha.service;

import ee.ria.riha.domain.model.EntityType;
import ee.ria.riha.domain.model.IssueComment;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import ee.ria.riha.storage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Info system issue comment service.
 *
 * @author Valentin Suhnjov
 */
@Service
public class IssueCommentService {

    public static final Function<Comment, IssueComment> COMMENT_TO_ISSUE_COMMENT = comment -> {
        if (comment == null) {
            return null;
        }

        return IssueComment.builder()
                .id(comment.getComment_id())
                .dateCreated(comment.getCreation_date())
                .issueId(comment.getComment_parent_id())
                .comment(comment.getComment())
                .authorName(comment.getAuthor_name())
                .authorPersonalCode(comment.getAuthor_personal_code())
                .organizationName(comment.getOrganization_name())
                .organizationCode(comment.getOrganization_code())
                .build();
    };

    public static final Function<IssueComment, Comment> ISSUE_COMMENT_TO_COMMENT = issueComment -> {
        if (issueComment == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setType(EntityType.ISSUE_COMMENT.name());
        comment.setComment_id(issueComment.getId());
        comment.setComment_parent_id(issueComment.getIssueId());
        comment.setComment(issueComment.getComment());
        comment.setAuthor_name(issueComment.getAuthorName());
        comment.setAuthor_personal_code(issueComment.getAuthorPersonalCode());
        comment.setOrganization_name(issueComment.getOrganizationName());
        comment.setOrganization_code(issueComment.getOrganizationCode());

        return comment;
    };

    @Autowired
    private CommentRepository commentRepository;

    /**
     * List concrete info system concrete issue comments.
     *
     * @param issueId    issue id
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of issue comments
     */
    public PagedResponse<IssueComment> listIssueComments(Long issueId, Pageable pageable, Filterable filterable) {
        Filterable filter = new FilterRequest(filterable.getFilter(), filterable.getSort(), filterable.getFields())
                .addFilter(getIssueCommentTypeFilter())
                .addFilter(getIssueIdEqFilter(issueId));

        PagedResponse<Comment> response = commentRepository.list(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                                   response.getTotalElements(),
                                   response.getContent().stream()
                                           .map(COMMENT_TO_ISSUE_COMMENT)
                                           .collect(toList()));
    }

    /**
     * Creates comment associated with an issue
     *
     * @param issueId      an id of an issue
     * @param issueComment comment content
     * @return created comment
     */
    public IssueComment createIssueComment(Long issueId, IssueComment issueComment) {
        issueComment.setIssueId(issueId);
        Long issueCommentId = commentRepository.add(ISSUE_COMMENT_TO_COMMENT.apply(issueComment)).get(0);

        return COMMENT_TO_ISSUE_COMMENT.apply(commentRepository.get(issueCommentId));
    }

    /**
     * Get single issue comment by id
     *
     * @param issueCommentId id of a comment
     * @return single comment or null
     */
    public IssueComment getIssueCommentById(Long issueCommentId) {
        Comment comment = commentRepository.get(issueCommentId);

        if (EntityType.valueOf(comment.getType()) != EntityType.ISSUE_COMMENT) {
            throw new IllegalArgumentException("Not an issue comment");
        }

        return COMMENT_TO_ISSUE_COMMENT.apply(comment);
    }

    private String getIssueCommentTypeFilter() {
        return "type,=," + EntityType.ISSUE_COMMENT.name();
    }

    private String getIssueIdEqFilter(Long issueId) {
        return "comment_parent_id,=," + issueId;
    }

}
