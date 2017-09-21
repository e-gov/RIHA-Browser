package ee.ria.riha.service;

import ee.ria.riha.domain.model.EntityType;
import ee.ria.riha.domain.model.IssueEvent;
import ee.ria.riha.domain.model.IssueEventType;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Info system issue event service
 *
 * @author Valentin Suhnjov
 */
@Component
public class IssueEventService {

    public static final Function<Comment, IssueEvent> COMMENT_TO_ISSUE_EVENT = comment -> {
        if (comment == null) {
            return null;
        }

        return IssueEvent.builder()
                .id(comment.getComment_id())
                .type(comment.getSub_type() != null ? IssueEventType.valueOf(comment.getSub_type()) : null)
                .dateCreated(comment.getCreation_date())
                .issueId(comment.getComment_parent_id())
                .authorName(comment.getAuthor_name())
                .authorPersonalCode(comment.getAuthor_personal_code())
                .organizationName(comment.getOrganization_name())
                .organizationCode(comment.getOrganization_code())
                .build();
    };

    public static final Function<IssueEvent, Comment> ISSUE_EVENT_TO_COMMENT = issueEvent -> {
        if (issueEvent == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setType(EntityType.ISSUE_EVENT.name());
        comment.setSub_type(issueEvent.getType().name());
        comment.setComment_id(issueEvent.getId());
        comment.setComment_parent_id(issueEvent.getIssueId());
        comment.setAuthor_name(issueEvent.getAuthorName());
        comment.setAuthor_personal_code(issueEvent.getAuthorPersonalCode());
        comment.setOrganization_name(issueEvent.getOrganizationName());
        comment.setOrganization_code(issueEvent.getOrganizationCode());

        return comment;
    };

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Creates event associated with an issue.
     *
     * @param issueId    an id of an issue
     * @param issueEvent a new issue event
     * @return
     */
    public IssueEvent createEvent(Long issueId, IssueEvent issueEvent) {
        issueEvent.setIssueId(issueId);
        Long issueEventId = commentRepository.add(ISSUE_EVENT_TO_COMMENT.apply(issueEvent)).get(0);

        return COMMENT_TO_ISSUE_EVENT.apply(commentRepository.get(issueEventId));
    }
}
