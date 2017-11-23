package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;
import ee.ria.riha.service.notifications.model.NewIssueToAssessorsEmailNotification;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class NewIssueToAssessorsNotificationHandler extends SimpleHtmlEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-issue-notification-assessors-template.ftl";

    private Configuration freeMarkerConfiguration;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueToAssessorsEmailNotification.class;
    }

    @Override
    public String getText(EmailNotificationDataModel dataModel) {
        try {
            NewIssueToAssessorsEmailNotification newIssueToAssessorsDataModel = (NewIssueToAssessorsEmailNotification) dataModel;
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();

            model.put("baseUrl", newIssueToAssessorsDataModel.getBaseUrl());
            model.put("name", newIssueToAssessorsDataModel.getInfoSystemFullName());
            model.put("shortName", newIssueToAssessorsDataModel.getInfoSystemShortName());
            model.put("title", newIssueToAssessorsDataModel.getIssueTitle());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            throw new MailPreparationException("Error generating notification message text template " + TEMPLATE_NAME, e);
        }
    }

    @Autowired
    public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }
}
