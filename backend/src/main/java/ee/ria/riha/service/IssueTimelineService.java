package ee.ria.riha.service;

import ee.ria.riha.domain.model.Entity;
import ee.ria.riha.domain.model.EntityType;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import ee.ria.riha.storage.util.*;
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

    public PagedResponse<Entity> listTimeline(Long issueId, Pageable pageable) {
        Filterable filter = new FilterRequest(null, "comment_id", null)
                .addFilter(getIssueIdEqFilter(issueId));

        PagedResponse<Comment> response = commentRepository.list(pageable, filter);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                                   response.getTotalElements(),
                                   response.getContent().stream()
                                           .map(comment -> {
                                               if (comment.getType().equals(EntityType.ISSUE_COMMENT.name())) {
                                                   return IssueCommentService.COMMENT_TO_ISSUE_COMMENT.apply(comment);
                                               } else if (comment.getType().equals(
                                                       EntityType.ISSUE_EVENT.name())) {
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
