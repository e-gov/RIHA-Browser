package ee.ria.riha.service.notification.handler;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewInfoSystemsEmailNotification;

@Component
public class NewInfoSystemsNotificationHandler extends IssueNotificationHandler<NewInfoSystemsEmailNotification> {

    @Override
    protected String getTemplateName() {
        return "new-info-systems-notification-template.ftl";
    }

    @Override
    protected String getSubjectKey(NewInfoSystemsEmailNotification notification) {
        return "notifications.createdInfoSystemsOverview.subject";
    }

    @Override
    protected Object[] getSubjectArgs(NewInfoSystemsEmailNotification notification) {
        return null;
    }

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewInfoSystemsEmailNotification.class;
    }

    protected HashMap<String, Object> createTemplateModel(NewInfoSystemsEmailNotification notification) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("baseUrl", notification.getBaseUrl());
        model.put("infoSystems", notification.getInfoSystems());
        return model;
    }
}
