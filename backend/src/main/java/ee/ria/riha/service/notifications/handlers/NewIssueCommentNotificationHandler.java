package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.service.notifications.model.NewIssueCommentNotification;
import ee.ria.riha.service.notifications.model.NotificationDataModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@Slf4j
@Getter
public class NewIssueCommentNotificationHandler implements EmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-issue-comment-notification-template.ftl";
    private static final String SUBJECT_KEY = "notifications.newIssueComment.subject";

    private final String baseUrl;
    private final String from;

    private Configuration freeMarkerConfiguration;
    private MessageSource messageSource;
    private JavaMailSenderImpl mailSender;

    @Autowired
    public NewIssueCommentNotificationHandler(ApplicationProperties applicationProperties) {
        baseUrl = applicationProperties.getBaseUrl();
        Assert.hasText(baseUrl, "Base URL must be defined");

        from = applicationProperties.getNotification().getFrom();
        Assert.hasText(from, "Notification email sender must be defined");
    }

    @Override
    public boolean supports(NotificationDataModel model) {
        return model.getClass() == NewIssueCommentNotification.class;
    }

    @Override
    public MimeMessage createMessage(NotificationDataModel model) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        NewIssueCommentNotification dataModel = (NewIssueCommentNotification) model;

        try {
            helper.setFrom(from);
            helper.setTo(dataModel.getTo());
            helper.setSentDate(new Date());
            helper.setSubject(getSubject());
            helper.setText(getText(dataModel), true);
            return message;
        } catch (MessagingException e) {
            throw new MailPreparationException("Error preparing notification message", e);
        }
    }

    private String getSubject() {
        return messageSource.getMessage(SUBJECT_KEY, null, Locale.getDefault());
    }

    private String getText(NewIssueCommentNotification dataModel) {
        try {
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();

            model.put("baseUrl", baseUrl);
            model.put("name", dataModel.getInfoSystemFullName());
            model.put("shortName", dataModel.getInfoSystemShortName());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            throw new MailPreparationException("Error generating notification message text template " + TEMPLATE_NAME, e);
        }
    }

    @Autowired
    public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }
}
