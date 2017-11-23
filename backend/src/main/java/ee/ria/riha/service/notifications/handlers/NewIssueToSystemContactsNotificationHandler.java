package ee.ria.riha.service.notifications.handlers;

import ee.ria.riha.service.notifications.model.EmailNotificationDataModel;
import ee.ria.riha.service.notifications.model.NewIssueToSystemContactsEmailNotification;
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
public class NewIssueToSystemContactsNotificationHandler extends SimpleHtmlEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-issue-notification-system-contacts-template.ftl";

    private Configuration freeMarkerConfiguration;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueToSystemContactsEmailNotification.class;
    }

    @Override
    public String getText(EmailNotificationDataModel dataModel) {
        try {
            NewIssueToSystemContactsEmailNotification newIssueToSystemContactsDataModel = (NewIssueToSystemContactsEmailNotification) dataModel;
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();

            model.put("baseUrl", newIssueToSystemContactsDataModel.getBaseUrl());
            model.put("name", newIssueToSystemContactsDataModel.getInfoSystemFullName());
            model.put("shortName", newIssueToSystemContactsDataModel.getInfoSystemShortName());

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
