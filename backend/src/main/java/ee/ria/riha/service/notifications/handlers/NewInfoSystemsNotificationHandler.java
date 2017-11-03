package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.service.notifications.model.NewInfoSystemsNotification;
import ee.ria.riha.service.notifications.model.NotificationDataModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
@Getter
public class NewInfoSystemsNotificationHandler extends BaseEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-IS-broadcast-template.ftl";
    private static final String SUBJECT_KEY = "notifications.createdInfoSystemsOverview.subject";

    private final String baseUrl;
    private final String from;
    private final String[] to;

    private Configuration freeMarkerConfiguration;
    private MessageSource messageSource;

    public NewInfoSystemsNotificationHandler(ApplicationProperties applicationProperties) {
        baseUrl = applicationProperties.getBaseUrl();
        Assert.hasText(baseUrl, "Base URL must be defined");

        from = applicationProperties.getNotification().getFrom();
        Assert.hasText(from, "Notification email sender must be defined");

        to = applicationProperties.getNotification().getCreatedInfoSystemsOverview().getTo();
        Assert.notEmpty(to, "At least one recipient must be defined in the list of recipients");
    }

    @Override
    public boolean supports(NotificationDataModel model) {
        return model.getClass() == NewInfoSystemsNotification.class;
    }

    @Override
    String getFrom() {
        return from;
    }

    @Override
    String[] getTo() {
        return to;
    }

    @Override
    String getSubject() {
        return messageSource.getMessage(SUBJECT_KEY, null, Locale.getDefault());
    }

    @Override
    void setText(MimeMessageHelper helper) throws MessagingException {
        helper.setText(getText(), true);
    }

    private String getText() {
        try {
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();
            NewInfoSystemsNotification dataModel = (NewInfoSystemsNotification) super.getModel();

            model.put("baseUrl", baseUrl);
            model.put("infoSystems", dataModel.getInfoSystems());

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
}
