package ee.ria.riha.service.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds basic email data that is necessary for sending any simple html email notification.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleHtmlEmailNotification implements EmailNotificationDataModel {

    private String from;
    private String[] to;
    private String subject;
    private String[] cc;
    private String[] bcc;
}
