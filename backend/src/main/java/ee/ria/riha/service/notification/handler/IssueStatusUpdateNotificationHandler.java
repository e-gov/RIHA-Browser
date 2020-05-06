package ee.ria.riha.service.notification.handler;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.IssueStatusUpdateNotification;

/**
 * @author Valentin Suhnjov
 */
@Component
public class IssueStatusUpdateNotificationHandler extends IssueNotificationHandler<IssueStatusUpdateNotification> {

    @Override
    protected String getTemplateName() {
        return "issue-status-update-notification-template.ftl";
    }

    @Override
    protected String getSubjectKey(IssueStatusUpdateNotification notification) {
        return "notifications.issueStatusUpdate.subject";
    }

    @Override
    protected Object[] getSubjectArgs(IssueStatusUpdateNotification notification) {
        return new String[] { notification.getInfoSystem().getShortName() };
    }

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == IssueStatusUpdateNotification.class;
    }

    protected HashMap<String, Object> createTemplateModel(IssueStatusUpdateNotification notification) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("issue", notification.getIssue());
        model.put("infoSystem", notification.getInfoSystem());
        model.put("commented", notification.isCommented());
        model.put("baseUrl", notification.getBaseUrl());
        model.put("uuid", notification.getInfoSystemUuid());
        return model;
    }
}
