package ee.ria.riha.service;

import ee.ria.riha.domain.CommentRepository;
import ee.ria.riha.domain.model.Comment;
import ee.ria.riha.domain.model.IssueEntity;
import ee.ria.riha.domain.model.IssueEntityType;
import ee.ria.riha.service.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

/**
 * Service responsible for issue timeline.
 *
 * @author Valentin Suhnjov
 */
@Service
public class IssueTimelineService {

    @Autowired
    private CommentRepository commentRepository;

    public PagedResponse<IssueEntity> listTimeline(Long issueId, Pageable pageable) {
        Filterable filter = new FilterRequest(null, "comment_id", null)
                .addFilter(getIssueIdEqFilter(issueId));

        PagedResponse<Comment> response = commentRepository.list(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                                   response.getTotalElements(),
                                   response.getContent().stream()
                                           .map(comment -> {
                                               if (comment.getType().equals(IssueEntityType.ISSUE_COMMENT.name())) {
                                                   return IssueCommentService.COMMENT_TO_ISSUE_COMMENT.apply(comment);
                                               } else if (comment.getType().equals(
                                                       IssueEntityType.ISSUE_EVENT.name())) {
                                                   return IssueEventService.COMMENT_TO_ISSUE_EVENT.apply(comment);
                                               }

                                               return null;
                                           })
                                           .collect(toList()));
    }

    private String getIssueIdEqFilter(Long issueId) {
        return "comment_parent_id,=," + issueId;
    }

}
