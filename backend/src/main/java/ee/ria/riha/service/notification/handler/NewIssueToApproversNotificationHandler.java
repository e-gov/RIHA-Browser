package ee.ria.riha.service.notification.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueToApproversEmailNotification;

@Component
public class NewIssueToApproversNotificationHandler extends IssueNotificationHandler<NewIssueToApproversEmailNotification> {

    private static final String STANDARD_SUBJECT_KEY = "notifications.newIssue.toApprovers.subject";
    private static final String ESTABLISHMENT_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.establishmentRequest.subject";
    private static final String TAKE_INTO_USE_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.takeIntoUseRequest.subject";
    private static final String MODIFICATION_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.modificationRequest.subject";
    private static final String FINALIZATION_REQUEST_SUBJECT_KEY = "notifications.newApprovalRequestIssue.finalizationRequest.subject";

    @Override
    protected String getTemplateName() {
        return "new-issue-notification-approvers-template.ftl";
    }

    @Override
    protected String getSubjectKey(NewIssueToApproversEmailNotification notification) {
        if (notification.getIssue() == null || notification.getIssue().getType() == null) {
            return STANDARD_SUBJECT_KEY;
        }

        switch (notification.getIssue().getType()) {
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
    protected Object[] getSubjectArgs(NewIssueToApproversEmailNotification notification) {
        return new String[] { notification.getInfoSystemShortName() };
    }

    @Override
    public boolean supports(EmailNotificationDataModel model) {
        return model.getClass() ==  NewIssueToApproversEmailNotification.class;
    }

    @Override
    protected Map<String, Object> createTemplateModel(NewIssueToApproversEmailNotification notification) {
        Map<String, Object> model = new HashMap<>();
        model.put("baseUrl", notification.getBaseUrl());
        model.put("name", notification.getInfoSystemFullName());
        model.put("shortName", notification.getInfoSystemShortName());
        model.put("issue", notification.getIssue());
        return model;
    }
}
