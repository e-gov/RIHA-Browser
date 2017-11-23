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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Getter
@Slf4j
public class NotificationService {

    private static final String NEW_ISSUE_COMMENT_SUBJECT_KEY = "notifications.newIssueComment.subject";
    private static final String NEW_ISSUE_SYSTEM_CONTACTS_SUBJECT_KEY = "notifications.newIssue.toSystemContacts.subject";
    private static final String NEW_ISSUE_ASSESSORS_SUBJECT_KEY = "notifications.newIssue.toAssessors.subject";

    private final String baseUrl;
    private final String from;
    private final boolean newIssueNotificationEnabled;
    private final boolean newIssueCommentNotificationEnabled;

    private EmailNotificationSenderService emailNotificationSenderService;
    private IssueService issueService;
    private UserService userService;
    private InfoSystemService infoSystemService;
    private MessageSource messageSource;

    @Autowired
    public NotificationService(ApplicationProperties applicationProperties) {
        baseUrl = applicationProperties.getBaseUrl();
        Assert.hasText(baseUrl, "Base URL must be defined");

        from = applicationProperties.getNotification().getFrom();
        Assert.hasText(from, "Notification email sender must be defined");

        newIssueNotificationEnabled = applicationProperties.getNotification().getNewIssue().isEnabled();
        newIssueCommentNotificationEnabled = applicationProperties.getNotification().getNewIssueComment().isEnabled();
    }

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
        NewIssueCommentEmailNotification notificationModel = NewIssueCommentEmailNotification.builder()
                .from(from)
                .to(emails.toArray(new String[emails.size()]))
                .subject(messageSource.getMessage(NEW_ISSUE_COMMENT_SUBJECT_KEY, new String[]{infoSystem.getShortName()}, Locale.getDefault()))
                .infoSystemFullName(infoSystem.getFullName())
                .infoSystemShortName(infoSystem.getShortName())
                .baseUrl(baseUrl)
                .build();

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

        NewIssueToSystemContactsEmailNotification notificationModel = NewIssueToSystemContactsEmailNotification.builder()
                .from(from)
                .to(to.toArray(new String[to.size()]))
                .subject(messageSource.getMessage(NEW_ISSUE_SYSTEM_CONTACTS_SUBJECT_KEY, null, Locale.getDefault()))
                .infoSystemFullName(infoSystem.getFullName())
                .infoSystemShortName(infoSystem.getShortName())
                .baseUrl(baseUrl)
                .build();

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueToAssessorsNotification(String issueTitle, InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        Set<String> assessorsEmails = userService.getAssessorsEmails();

        NewIssueToAssessorsEmailNotification notificationModel = NewIssueToAssessorsEmailNotification.builder()
                .from(from)
                .to(assessorsEmails.toArray(new String[assessorsEmails.size()]))
                .subject(messageSource.getMessage(NEW_ISSUE_ASSESSORS_SUBJECT_KEY, new String[]{infoSystem.getShortName()}, Locale.getDefault()))
                .infoSystemFullName(infoSystem.getFullName())
                .infoSystemShortName(infoSystem.getShortName())
                .issueTitle(issueTitle)
                .baseUrl(baseUrl)
                .build();

        emailNotificationSenderService.sendNotification(notificationModel);
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
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
