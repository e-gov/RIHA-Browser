package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue comment email notification messages.
 */
@Getter
@Setter
public class NewIssueCommentEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private String baseUrl;
}
