package ee.ria.riha.service.notification.model;

import ee.ria.riha.domain.model.IssueResolutionType;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue decision email notification messages.
 */
@Getter
@Setter
public class NewIssueDecisionEmailNotification extends NewIssueCommentEmailNotification {

	private IssueResolutionType decision;
}
