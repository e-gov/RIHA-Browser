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
            helper.setFrom(getFrom(model));
            helper.setTo(getTo(model));
            helper.setSentDate(getSentDate(model));
            if (getCc(model) != null) helper.setCc(getCc(model));
            if (getBcc(model) != null) helper.setBcc(getBcc(model));
            helper.setSubject(getSubject(model));
            helper.setText(getText(model), true);
            return message;
        } catch (MessagingException e) {
            throw new MailPreparationException("Error preparing notification message", e);
        }
    }

    protected String getFrom(EmailNotificationDataModel model) {
        return model.getFrom();
    }

    protected String[] getTo(EmailNotificationDataModel model) {
        return model.getTo();
    }

    protected Date getSentDate(EmailNotificationDataModel model) {
        return new Date();
    }

    protected String[] getCc(EmailNotificationDataModel model) {
        return model.getCc();
    }

    protected String[] getBcc(EmailNotificationDataModel model) {
        return model.getBcc();
    }

    protected abstract String getSubject(EmailNotificationDataModel model);

    protected abstract String getText(EmailNotificationDataModel model);

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }
}
