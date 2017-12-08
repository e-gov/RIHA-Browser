package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;
import ee.ria.riha.service.notifications.model.NewIssueToApproversEmailNotification;
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
import java.util.Map;

@Component
public class NewIssueToApproversNotificationHandler extends SimpleHtmlEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-issue-notification-approvers-template.ftl";
    private static final String SUBJECT_KEY = "notifications.newIssue.toApprovers.subject";

    private Configuration freeMarkerConfiguration;
    private MessageSource messageSource;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueToApproversEmailNotification.class;
    }

    @Override
    protected String getSubject(EmailNotificationDataModel model) {
        NewIssueToApproversEmailNotification newIssueToApproversDataModel = (NewIssueToApproversEmailNotification) model;
        return messageSource.getMessage(SUBJECT_KEY,
                new String[]{newIssueToApproversDataModel.getInfoSystemShortName()}, Locale.getDefault());
    }

    @Override
    protected String getText(EmailNotificationDataModel dataModel) {
        try {
            NewIssueToApproversEmailNotification newIssueToApproversDataModel = (NewIssueToApproversEmailNotification) dataModel;
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();

            model.put("baseUrl", newIssueToApproversDataModel.getBaseUrl());
            model.put("name", newIssueToApproversDataModel.getInfoSystemFullName());
            model.put("shortName", newIssueToApproversDataModel.getInfoSystemShortName());
            model.put("title", newIssueToApproversDataModel.getIssueTitle());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            throw new NotificationHandlerException(
                    "Error generating notification message text template " + TEMPLATE_NAME, e);
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
