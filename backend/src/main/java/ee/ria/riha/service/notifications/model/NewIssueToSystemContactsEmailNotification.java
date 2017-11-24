package ee.ria.riha.service.notifications.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue email notification messages to info system contacts.
 */
@Getter
@Setter
public class NewIssueToSystemContactsEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private String baseUrl;
}
