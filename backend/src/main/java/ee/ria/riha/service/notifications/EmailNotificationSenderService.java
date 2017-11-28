package ee.ria.riha.service.notifications;

import ee.ria.riha.service.notifications.handlers.EmailNotificationHandler;
import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@Slf4j
@Getter
public class EmailNotificationSenderService {

    private EmailNotificationHandler[] handlers;
    private JavaMailSenderImpl mailSender;

    @Async
    public void sendNotification(EmailNotificationDataModel model) {
        Assert.notNull(model, "Failed to send message as data model object was not defined.");

        try {
            EmailNotificationHandler handler = findAppropriateHandler(model)
                    .orElseThrow(() -> new MailPreparationException(
                            "Failed to send message as appropriate email notification handler was not found."));

            MimeMessage message = handler.createMessage(model);

            if (log.isDebugEnabled()) {
                log.debug("Sending notification message from '{}' to '{}' with subject '{}'",
                        message.getFrom(), message.getAllRecipients(), message.getSubject());
            }

            mailSender.send(message);
            log.info("Notification message has been successfully sent.");
        } catch (MessagingException | MailPreparationException e) {
            log.warn("Failed to send notification message.", e);
        }
    }

    private Optional<EmailNotificationHandler> findAppropriateHandler(EmailNotificationDataModel model) {
        for (EmailNotificationHandler handler : handlers) {
            if (handler.supports(model)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setHandlers(EmailNotificationHandler[] handlers) {
        this.handlers = handlers;
    }
}
