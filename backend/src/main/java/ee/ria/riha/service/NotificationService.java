package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.notifications.EmailNotificationSenderService;
import ee.ria.riha.service.notifications.model.NewInfoSystemsEmailNotification;
import ee.ria.riha.service.notifications.model.NewIssueCommentEmailNotification;
import ee.ria.riha.service.notifications.model.NewIssueToAssessorsEmailNotification;
import ee.ria.riha.service.notifications.model.NewIssueToSystemContactsEmailNotification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@Getter
@Slf4j
public class NotificationService {

    private EmailNotificationSenderService emailNotificationSenderService;
    private IssueService issueService;
    private UserService userService;
    private InfoSystemService infoSystemService;
    private ApplicationProperties applicationProperties;

    public void sendNewInfoSystemsNotification(NewInfoSystemsEmailNotification notificationModel) {
        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueCommentNotification(Long issueId) {
        if (!isNewIssueCommentNotificationEnabled()) {
            log.info("New issue comment notifications sending is disabled.");
            return;
        }

        Set<String> participantsPersonalCodes = issueService.getParticipantsPersonalCodes(issueId);
        Set<String> emails = userService.getEmailsByPersonalCodes(participantsPersonalCodes);

        if (emails.isEmpty()) {
            log.info("New issue comment has been recently added for issue with id {}, but none of issue participants have emails.", issueId);
            return;
        }

        InfoSystem infoSystem = infoSystemService.getByIssueId(issueId);

        NewIssueCommentEmailNotification notificationModel = new NewIssueCommentEmailNotification();
        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(emails.toArray(new String[emails.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueToSystemContactsNotification(InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        List<String> to = infoSystemService.getSystemContactsEmails(infoSystem);
        if (to.isEmpty()) {
            log.info("New issue has been recently added, but info system '{}' has no contacts.", infoSystem.getFullName());
            return;
        }

        NewIssueToSystemContactsEmailNotification notificationModel = new NewIssueToSystemContactsEmailNotification();
        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(to.toArray(new String[to.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueToAssessorsNotification(String issueTitle, InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        Set<String> assessorsEmails = userService.getAssessorsEmails();

        NewIssueToAssessorsEmailNotification notificationModel = new NewIssueToAssessorsEmailNotification();
        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(assessorsEmails.toArray(new String[assessorsEmails.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setIssueTitle(issueTitle);
        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    private String getDefaultNotificationSender() {
        String defaultNotificationSender = applicationProperties.getNotification().getFrom();
        Assert.hasText(defaultNotificationSender, "Notification email sender must be defined");
        return defaultNotificationSender;
    }

    private String getBaseUrl() {
        String baseUrl = applicationProperties.getBaseUrl();
        Assert.hasText(baseUrl, "Base URL must be defined");
        return baseUrl;
    }

    private boolean isNewIssueNotificationEnabled() {
        return applicationProperties.getNotification().getNewIssue().isEnabled();
    }

    private boolean isNewIssueCommentNotificationEnabled() {
        return applicationProperties.getNotification().getNewIssueComment().isEnabled();
    }

    @Autowired
    public void setEmailNotificationSenderService(EmailNotificationSenderService emailNotificationSenderService) {
        this.emailNotificationSenderService = emailNotificationSenderService;
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setInfoSystemService(InfoSystemService infoSystemService) {
        this.infoSystemService = infoSystemService;
    }

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
}
