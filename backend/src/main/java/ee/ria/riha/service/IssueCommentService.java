package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.model.IssueComment;
import ee.ria.riha.domain.model.IssueEntityType;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import ee.ria.riha.storage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;
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
        comment.setType(IssueEntityType.ISSUE_COMMENT.name());
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
     * @param issueId an id of an issue
     * @param comment comment
     * @return created comment
     */
    public IssueComment createIssueComment(Long issueId, String comment) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        if (rihaUserDetails == null) {
            throw new IllegalBrowserStateException("User details not present in security context");
        }

        RihaOrganization organization = getActiveOrganization();
        if (organization == null) {
            throw new ValidationException("validation.generic.activeOrganization.notSet");
        }

        IssueComment issueComment = IssueComment.builder()
                .issueId(issueId)
                .comment(comment)
                .authorName(rihaUserDetails.getFullName())
                .authorPersonalCode(rihaUserDetails.getPersonalCode())
                .organizationName(organization.getName())
                .organizationCode(organization.getCode())
                .build();

        List<Long> createdIssueCommentIds = commentRepository.add(ISSUE_COMMENT_TO_COMMENT.apply(issueComment));
        if (createdIssueCommentIds.isEmpty()) {
            throw new IllegalBrowserStateException("Issue comment was not created");
        }

        return COMMENT_TO_ISSUE_COMMENT.apply(commentRepository.get(createdIssueCommentIds.get(0)));
    }

    /**
     * Get single issue comment by id
     *
     * @param issueCommentId id of a comment
     * @return single comment or null
     */
    public IssueComment getIssueCommentById(Long issueCommentId) {
        Comment comment = commentRepository.get(issueCommentId);

        if (IssueEntityType.valueOf(comment.getType()) != IssueEntityType.ISSUE_COMMENT) {
            throw new IllegalArgumentException("Not an issue comment");
        }

        return COMMENT_TO_ISSUE_COMMENT.apply(comment);
    }

    private String getIssueCommentTypeFilter() {
        return "type,=," + IssueEntityType.ISSUE_COMMENT.name();
    }

    private String getIssueIdEqFilter(Long issueId) {
        return "comment_parent_id,=," + issueId;
    }

}
