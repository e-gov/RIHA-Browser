package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.NotificationDataModel;

import javax.mail.internet.MimeMessage;

/**
 * Interface for classes that prepare notification messages.
 */
public interface EmailNotificationHandler {

    boolean supports(NotificationDataModel model);

    MimeMessage createMessage(NotificationDataModel model);
}
