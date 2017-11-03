package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.NotificationDataModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Getter
@Slf4j
public abstract class BaseEmailNotificationHandler {

    private MimeMessage message;
    private NotificationDataModel model;

    public void prepareMessage(MimeMessage message, NotificationDataModel model) {
        MimeMessageHelper helper = new MimeMessageHelper(message);

        this.message = message;
        this.model = model;

        try {
            helper.setFrom(getFrom());
            helper.setTo(getTo());
            helper.setSentDate(new Date());
            helper.setSubject(getSubject());
            setText(helper);
        } catch (MessagingException e) {
            throw new MailPreparationException("Error preparing notification message", e);
        }
    }

    public abstract boolean supports(NotificationDataModel model);

    abstract String getFrom();

    abstract String[] getTo();

    abstract String getSubject();

    abstract void setText(MimeMessageHelper helper) throws MessagingException;
}
