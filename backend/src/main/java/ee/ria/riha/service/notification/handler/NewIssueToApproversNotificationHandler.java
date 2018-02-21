package ee.ria.riha.service.notification.handler;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueToApproversEmailNotification;
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
    private static final String STANDARD_SUBJECT_KEY = "notifications.newIssue.toApprovers.subject";

    private static final String ESTABLISHMENT_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.establishmentRequest.subject";
    private static final String TAKE_INTO_USE_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.takeIntoUseRequest.subject";
    private static final String MODIFICATION_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.modificationRequest.subject";
    private static final String FINALIZATION_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.finalizationRequest.subject";

    private Configuration freeMarkerConfiguration;
    private MessageSource messageSource;

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() == NewIssueToApproversEmailNotification.class;
    }

    @Override
    protected String getSubject(EmailNotificationDataModel model) {
        NewIssueToApproversEmailNotification newIssueToApproversDataModel = (NewIssueToApproversEmailNotification) model;

        String subjectKey = getSubjectKey(newIssueToApproversDataModel);
        return messageSource.getMessage(subjectKey,
                new String[]{newIssueToApproversDataModel.getInfoSystemShortName()},
                Locale.getDefault());
    }

    private String getSubjectKey(NewIssueToApproversEmailNotification model) {
        if (model.getIssue() == null || model.getIssue().getType() == null) {
            return STANDARD_SUBJECT_KEY;
        }

        switch (model.getIssue().getType()) {
            case "ESTABLISHMENT_REQUEST":
                return ESTABLISHMENT_REQUEST_SUBJECT_KEY;
            case "TAKE_INTO_USE_REQUEST":
                return TAKE_INTO_USE_REQUEST_SUBJECT_KEY;
            case "MODIFICATION_REQUEST":
                return MODIFICATION_REQUEST_SUBJECT_KEY;
            case "FINALIZATION_REQUEST":
                return FINALIZATION_REQUEST_SUBJECT_KEY;
            default:
                return STANDARD_SUBJECT_KEY;
        }
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
            model.put("issue", newIssueToApproversDataModel.getIssue());

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
