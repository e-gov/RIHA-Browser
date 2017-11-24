package ee.ria.riha.service.notifications.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue email notification messages to RIHA assessors.
 */
@Getter
@Setter
public class NewIssueToAssessorsEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private String issueTitle;
    private String baseUrl;
}
