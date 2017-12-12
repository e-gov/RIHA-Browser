package ee.ria.riha.service;

import ee.ria.riha.domain.model.IssueEntityType;
import ee.ria.riha.domain.model.IssueEvent;
import ee.ria.riha.domain.model.IssueEventType;
import ee.ria.riha.domain.model.IssueResolutionType;
import ee.ria.riha.storage.domain.CommentRepository;
import ee.ria.riha.storage.domain.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
                .resolutionType(comment.getResolution_type() != null
                        ? IssueResolutionType.valueOf(comment.getResolution_type())
                        : null)
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
        comment.setType(IssueEntityType.ISSUE_EVENT.name());
        comment.setSub_type(issueEvent.getType().name());
        comment.setComment_id(issueEvent.getId());
        comment.setComment_parent_id(issueEvent.getIssueId());
        comment.setAuthor_name(issueEvent.getAuthorName());
        comment.setAuthor_personal_code(issueEvent.getAuthorPersonalCode());
        comment.setOrganization_name(issueEvent.getOrganizationName());
        comment.setOrganization_code(issueEvent.getOrganizationCode());

        if (issueEvent.getResolutionType() != null) {
            comment.setResolution_type(issueEvent.getResolutionType().name());
        }

        return comment;
    };

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Creates event associated with an issue.
     *
     * @param issueId    an id of an issue
     * @param issueEvent a new issue event
     * @return created issue event
     */
    public IssueEvent createEvent(Long issueId, IssueEvent issueEvent) {
        issueEvent.setIssueId(issueId);
        List<Long> createdIssueEventIds = commentRepository.add(ISSUE_EVENT_TO_COMMENT.apply(issueEvent));
        if (createdIssueEventIds.isEmpty()) {
            throw new IllegalBrowserStateException("Issue event was not created");
        }

        return get(createdIssueEventIds.get(0));
    }

    /**
     * Retrieves single issue event by its id.
     *
     * @param id issue id event
     * @return IssueEvent
     */
    public IssueEvent get(Long id) {
        Comment comment = commentRepository.get(id);

        if (IssueEntityType.valueOf(comment.getType()) != IssueEntityType.ISSUE_EVENT) {
            throw new IllegalBrowserStateException("Not an issue event");
        }

        return COMMENT_TO_ISSUE_EVENT.apply(comment);
    }
}
