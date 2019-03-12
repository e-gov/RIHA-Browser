package ee.ria.riha.service.notification.handler;

import org.springframework.mail.javamail.MimeMessagePreparator;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;

/**
 * Interface for classes that prepare email notification messages
 */
public interface EmailNotificationHandler<T extends EmailNotificationDataModel> {

    /**
     * Checks if handler supports message creation from given notification data model
     *
     * @param model notification data
     * @return true if handler supports message creation from this notification data model, false otherwise
     */
    boolean supports(EmailNotificationDataModel model);

    /**
     * Creates {@link MimeMessagePreparator} for given notification data model
     *
     * @param model notification data
     * @return instance of {@link MimeMessagePreparator}
     */
    MimeMessagePreparator createMessagePreparator(T model);
}
