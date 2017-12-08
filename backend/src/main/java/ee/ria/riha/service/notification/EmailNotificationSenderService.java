package ee.ria.riha.service.notification;

import ee.ria.riha.service.notification.handler.EmailNotificationHandler;
import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
@Slf4j
public class EmailNotificationSenderService {

    private JavaMailSenderImpl mailSender;

    private List<EmailNotificationHandler> handlers = new ArrayList<>();

    @Autowired
    public EmailNotificationSenderService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public Future<Void> sendNotification(EmailNotificationDataModel model) {
        Assert.notNull(model, "Email notification data model must be provided");

        try {
            EmailNotificationHandler handler = findMessageHandler(model)
                    .orElseThrow(() -> new MailPreparationException(
                            "Notification handler for model of type " + model.getClass().getName() + " was not found"));

            MimeMessage message = prepareMessage(handler, model);
            sendMessage(message);
        } catch (MailException e) {
            log.warn("Error sending notification message", e);
        }

        return AsyncResult.forValue(null);
    }

    private Optional<EmailNotificationHandler> findMessageHandler(EmailNotificationDataModel model) {
        for (EmailNotificationHandler handler : handlers) {
            if (handler.supports(model)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    private MimeMessage prepareMessage(EmailNotificationHandler handler, EmailNotificationDataModel model) {
        try {
            log.debug("Using handler {} to create preparator for model {}", handler, model.getClass().getName());
            MimeMessagePreparator preparator = handler.createMessagePreparator(model);

            if (preparator == null) {
                throw new MailPreparationException(
                        "Handler " + handler + " did not produce preparator for model " + model.getClass().getName());
            }

            MimeMessage message = mailSender.createMimeMessage();
            preparator.prepare(message);
            return message;
        } catch (Exception e) {
            throw new MailPreparationException("Could not prepare message", e);
        }
    }

    private void sendMessage(MimeMessage message) {
        log.info("Sending notification message {}", messageToString(message));
        mailSender.send(message);
        log.info("Notification message {} was successfully sent", messageToString(message));
    }

    private String messageToString(Message message) {
        try {
            return String.format("[from: '%s', to: '%s', subject: '%s']",
                    Arrays.toString(message.getFrom()),
                    Arrays.toString(message.getAllRecipients()),
                    message.getSubject());
        } catch (MessagingException e) {
            log.info("Unable to produce string representation of message {} due to {}", message, e.toString());
            return message.toString();
        }
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public List<EmailNotificationHandler> getHandlers() {
        return handlers;
    }

    @Autowired
    public void setHandlers(List<EmailNotificationHandler> handlers) {
        this.handlers = handlers;
    }
}
