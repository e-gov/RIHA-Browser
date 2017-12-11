package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;

import javax.mail.internet.MimeMessage;

/**
 * Interface for classes that prepare email notification messages.
 */
public interface EmailNotificationHandler {

    boolean supports(EmailNotificationDataModel model);

    MimeMessage createMessage(EmailNotificationDataModel model);
}
