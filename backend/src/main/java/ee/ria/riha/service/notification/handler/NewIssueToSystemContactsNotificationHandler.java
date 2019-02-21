package ee.ria.riha.service.notification.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueToSystemContactsEmailNotification;

@Component
public class NewIssueToSystemContactsNotificationHandler extends IssueNotificationHandler<NewIssueToSystemContactsEmailNotification> {

    @Override
    protected String getTemplateName() {
        return "new-issue-notification-system-contacts-template.ftl";
    }

    @Override
    protected String getSubjectKey(NewIssueToSystemContactsEmailNotification notification) {
        return "notifications.newIssue.toSystemContacts.subject";
    }

    @Override
    protected Object[] getSubjectArgs(NewIssueToSystemContactsEmailNotification notification) {
        return null;
    }

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() ==  NewIssueToSystemContactsEmailNotification.class;
    }

    @Override
    protected Map<String, Object> createTemplateModel(NewIssueToSystemContactsEmailNotification notification) {
        Map<String, Object> model = new HashMap<>();
        model.put("baseUrl", notification.getBaseUrl());
        model.put("name", notification.getInfoSystemFullName());
        model.put("shortName", notification.getInfoSystemShortName());
        return model;
    }
}
