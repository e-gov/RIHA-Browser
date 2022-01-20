package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds basic email data that is necessary for sending any simple html email notification.
 */
@Getter
@Setter
public class SimpleHtmlEmailNotification implements EmailNotificationDataModel {

    private String from;
    private String to;
    private String[] cc;
    private String[] bcc;
}
