package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue email notification messages to RIHA approvers.
 */
@Getter
@Setter
public class NewIssueToApproversEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private IssueDataModel issue;
    private String baseUrl;
}
