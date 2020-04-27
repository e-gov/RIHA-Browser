package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

/**
 * Holds data for new issue email notification messages to info system contacts.
 */
@Getter
@Setter
public class NewIssueToSystemContactsEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String baseUrl;
    private UUID infoSystemUuid;
}
