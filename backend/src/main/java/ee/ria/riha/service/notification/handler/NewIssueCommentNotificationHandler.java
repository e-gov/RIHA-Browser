package ee.ria.riha.service.notification.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueCommentEmailNotification;

@Component
public class NewIssueCommentNotificationHandler extends IssueNotificationHandler<NewIssueCommentEmailNotification> {

    @Override
    protected String getTemplateName() {
        return "new-issue-comment-notification-template.ftl";
    }

    @Override
    protected String getSubjectKey(NewIssueCommentEmailNotification notification) {
        return "notifications.newIssueComment.subject";
    }

    @Override
    protected Object[] getSubjectArgs(NewIssueCommentEmailNotification notification) {
        return new String[] { notification.getInfoSystemShortName() };
    }

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueCommentEmailNotification.class;
    }

    @Override
    protected Map<String, Object> createTemplateModel(NewIssueCommentEmailNotification notification) {
        Map<String, Object> model = new HashMap<>();
        model.put("baseUrl", notification.getBaseUrl());
        model.put("name", notification.getInfoSystemFullName());
        model.put("shortName", notification.getInfoSystemShortName());
        model.put("issueId", notification.getIssueId());
        model.put("issueTitle", notification.getIssueTitle());
        model.put("author", notification.getAuthorName());
        model.put("comment", notification.getComment());
        return model;
    }
}
