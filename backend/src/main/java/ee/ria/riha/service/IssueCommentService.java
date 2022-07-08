package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.CommentRepository;
import ee.ria.riha.domain.model.Comment;
import ee.ria.riha.domain.model.IssueComment;
import ee.ria.riha.domain.model.IssueEntityType;
import ee.ria.riha.service.util.*;
import ee.ria.riha.web.model.DashboardIssueComment;
import ee.ria.riha.web.model.IssueCommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static final Function<Comment, DashboardIssueComment> COMMENT_TO_DASHBOARD_ISSUE_COMMENT = comment -> {
        if (comment == null) {
            return null;
        }

        return DashboardIssueComment.builder()
                .id(comment.getLast_comment_id())
                .dateCreated(comment.getLast_comment_creation_date())
                .authorName(comment.getLast_comment_author_name())
                .organizationName(comment.getLast_comment_organization_name())
                .organizationCode(comment.getLast_comment_organization_code())
                .build();
    };

    private CommentRepository commentRepository;

    private NotificationService notificationService;

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

    private String getIssueCommentTypeFilter() {
        return "type,=," + IssueEntityType.ISSUE_COMMENT.name();
    }

    private String getIssueIdEqFilter(Long issueId) {
        return "comment_parent_id,=," + issueId;
    }

    /**
     * Creates comment associated with an issue. Sends notification about created issue comment.
     *
     * @param issueId an id of an issue
     * @param model   model of an issue comment
     * @return created comment
     */
    public IssueComment createIssueComment(Long issueId, IssueCommentModel model) {
        IssueComment createdIssueComment = createIssueCommentWithoutNotification(issueId, model);

        notificationService.sendNewIssueCommentNotification(createdIssueComment);

        return createdIssueComment;
    }

    /**
     * Creates comment associated with an issue but does not send notification
     *
     * @param issueId an id of an issue
     * @param model   issue comment model
     * @return created comment
     */
    public IssueComment createIssueCommentWithoutNotification(Long issueId, IssueCommentModel model) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails()
                .orElseThrow(() -> new IllegalBrowserStateException("User details not present in security context"));
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));

        IssueComment issueComment = IssueComment.builder()
                .issueId(issueId)
                .comment(model.getComment())
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

    /**
     * List concrete info system concrete issue comments.
     *
     * @param issueId - issue id
     * @return list of issue comments related to provided issue id
     */
    public List<IssueComment> listByIssueId(Long issueId) {
        FilterRequest request = new FilterRequest();
        request.addFilter(getIssueIdEqFilter(issueId));

        return commentRepository.find(request).stream()
                .map(COMMENT_TO_ISSUE_COMMENT)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setNotificationService(@Lazy NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
