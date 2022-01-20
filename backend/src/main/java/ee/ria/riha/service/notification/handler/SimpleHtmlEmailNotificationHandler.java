package ee.ria.riha.service.notification.handler;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.Assert;

import ee.ria.riha.service.notification.model.SimpleHtmlEmailNotification;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public abstract class SimpleHtmlEmailNotificationHandler<T extends SimpleHtmlEmailNotification> implements EmailNotificationHandler<T> {

    @Override
    public MimeMessagePreparator createMessagePreparator(T model) {
        return mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            setTo(helper, model);
            setFrom(helper, model);
            setCc(helper, model);
            setBcc(helper, model);

            setSubject(helper, model);
            setText(helper, model);
        };
    }

    /**
     * Populates message with recipient addresses. At least one recipient address must be provided.
     *
     * @param helper message helper
     * @param model  message data model
     * @throws IllegalArgumentException     in case recipient addresses were not provided
     * @throws NotificationHandlerException in case of exception while setting recipient addresses
     */
    protected void setTo(MimeMessageHelper helper, T model) {
        Assert.notNull(model.getTo(), "List of recipients must not be empty");

        try {
            helper.setTo(model.getTo());
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message recipients addresses", e);
        }
    }

    /**
     * Populates message with sender address
     *
     * @param helper message helper
     * @param model  message data model
     * @throws IllegalArgumentException     in case sender address was not provided
     * @throws NotificationHandlerException in case of exception while setting sender address
     */
    protected void setFrom(MimeMessageHelper helper, T model) {
        Assert.notNull(model.getFrom(), "Sender address must be provided");

        try {
            helper.setFrom(model.getFrom());
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message sender address", e);
        }
    }

    /**
     * Populates message with CC addresses. CC addresses are optional and will not be set if model does not contain
     * them.
     *
     * @param helper message helper
     * @param model  message data model
     * @throws NotificationHandlerException in case of exception while setting CC addresses
     */
    protected void setCc(MimeMessageHelper helper, T model) {
        String[] cc = model.getCc();
        if (cc == null) {
            return;
        }

        try {
            helper.setCc(cc);
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message cc", e);
        }
    }

    /**
     * Populates message with BCC addresses. BCC addresses are optional and will not be set if model does not contain
     * them.
     *
     * @param helper message helper
     * @param model  message data model
     * @throws NotificationHandlerException in case of exception while setting BCC addresses
     */
    protected void setBcc(MimeMessageHelper helper, T model) {
        String[] bcc = model.getBcc();
        if (bcc == null) {
            return;
        }

        try {
            helper.setBcc(bcc);
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message bcc", e);
        }
    }

    /**
     * Populates message with subject
     *
     * @param helper message helper
     * @param model  message data model
     * @throws NotificationHandlerException in case of exception while setting message subject
     */
    protected void setSubject(MimeMessageHelper helper, T model) {
        try {
            helper.setSubject(getSubject(model));
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message subject", e);
        }
    }

    /**
     * Populates message with body text
     *
     * @param helper message helper
     * @param model  message data model
     * @throws NotificationHandlerException in case of exception while setting message text
     */
    protected void setText(MimeMessageHelper helper, T model) {
        try {
            helper.setText(getText(model), true);
        } catch (MessagingException e) {
            throw new NotificationHandlerException("Unable to set message text", e);
        }
    }

    /**
     * Retrieves message subject
     *
     * @param model notification model
     * @return message subject
     */
    protected abstract String getSubject(T model);

    /**
     * Retrives message body text
     *
     * @param model notification model
     * @return message body
     */
    protected abstract String getText(T model);
}
