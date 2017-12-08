package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.IssueStatusUpdateNotification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Valentin Suhnjov
 */
@Component
public class IssueStatusUpdateNotificationHandler extends SimpleHtmlEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "issue-status-update-notification-template.ftl";
    private static final String SUBJECT_KEY = "notifications.issueStatusUpdate.subject";

    private MessageSource messageSource;
    private Configuration freemarkerConfiguration;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model instanceof IssueStatusUpdateNotification;
    }

    @Override
    protected String getSubject(EmailNotificationDataModel model) {
        return messageSource.getMessage(SUBJECT_KEY, null, Locale.getDefault());
    }

    @Override
    protected String getText(EmailNotificationDataModel model) {
        try {
            Template template = freemarkerConfiguration.getTemplate(TEMPLATE_NAME);
            HashMap<String, Object> templateModel = createTemplateModel((IssueStatusUpdateNotification) model);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
        } catch (IOException | TemplateException e) {
            throw new NotificationHandlerException("Error generating message text using template " + TEMPLATE_NAME, e);
        }
    }

    private HashMap<String, Object> createTemplateModel(IssueStatusUpdateNotification notification) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("issue", notification.getIssue());
        result.put("infoSystem", notification.getInfoSystem());
        result.put("commented", notification.isCommented());
        result.put("baseUrl", notification.getBaseUrl());

        return result;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

}
