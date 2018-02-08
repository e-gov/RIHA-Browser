package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueCommentEmailNotification;
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
public class NewIssueCommentNotificationHandler extends SimpleHtmlEmailNotificationHandler {

    private static final String TEMPLATE_NAME = "new-issue-comment-notification-template.ftl";
    private static final String SUBJECT_KEY = "notifications.newIssueComment.subject";

    private Configuration freeMarkerConfiguration;
    private MessageSource messageSource;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueCommentEmailNotification.class;
    }

    @Override
    protected String getSubject(EmailNotificationDataModel model) {
        NewIssueCommentEmailNotification newIssueCommentDataModel = (NewIssueCommentEmailNotification) model;
        return messageSource.getMessage(SUBJECT_KEY, new String[]{newIssueCommentDataModel.getInfoSystemShortName()},
                Locale.getDefault());
    }

    @Override
    protected String getText(EmailNotificationDataModel dataModel) {
        try {
            NewIssueCommentEmailNotification newIssueCommentDataModel = (NewIssueCommentEmailNotification) dataModel;
            Template template = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME);
            Map<String, Object> model = new HashMap<>();

            model.put("baseUrl", newIssueCommentDataModel.getBaseUrl());
            model.put("name", newIssueCommentDataModel.getInfoSystemFullName());
            model.put("shortName", newIssueCommentDataModel.getInfoSystemShortName());
            model.put("issueId", newIssueCommentDataModel.getIssueId());
            model.put("issueTitle", newIssueCommentDataModel.getIssueTitle());
            model.put("author", newIssueCommentDataModel.getAuthorName());
            model.put("comment", newIssueCommentDataModel.getComment());

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
