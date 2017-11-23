package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public abstract class SimpleHtmlEmailNotificationHandler implements EmailNotificationHandler {

    private JavaMailSenderImpl mailSender;

    @Override
    public MimeMessage createMessage(EmailNotificationDataModel model) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(model.getFrom());
            helper.setTo(model.getTo());
            helper.setSentDate(new Date());
            if (model.getCc() != null) helper.setCc(model.getCc());
            if (model.getBcc() != null) helper.setBcc(model.getBcc());
            helper.setSubject(model.getSubject());
            helper.setText(getText(model), true);
            return message;
        } catch (MessagingException e) {
            throw new MailPreparationException("Error preparing notification message", e);
        }
    }

    public abstract String getText(EmailNotificationDataModel model);

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }
}
