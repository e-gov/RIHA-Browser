package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.domain.model.IssueComment;
import ee.ria.riha.domain.model.IssueEvent;
import ee.ria.riha.service.notification.EmailNotificationSenderService;
import ee.ria.riha.service.notification.model.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;

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

    public void sendNewIssueCommentNotification(IssueComment issueComment) {
        if (!isNewIssueCommentNotificationEnabled()) {
            log.info("New issue comment notifications sending is disabled.");
            return;
        }

        NewIssueCommentEmailNotification notificationModel = createIssueCommentNotification(
                        new NewIssueCommentEmailNotification(),
                        issueComment.getIssueId(),
                        issueComment.getComment());
        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueDecisionNotification(IssueEvent decisionEvent) {
        if (!isNewIssueDecisionNotificationEnabled()) {
            log.info("New issue decision notifications sending is disabled.");
            return;
        }

        NewIssueDecisionEmailNotification notificationModel = createIssueCommentNotification(
                        new NewIssueDecisionEmailNotification(),
                        decisionEvent.getIssueId(),
                        decisionEvent.getComment());
        if (notificationModel != null) {
            notificationModel.setDecision(decisionEvent.getResolutionType());
            emailNotificationSenderService.sendNotification(notificationModel);
        }
    }

    private <T extends NewIssueCommentEmailNotification> T createIssueCommentNotification(T notificationModel,
                                                                                          Long issueId,
                                                                                          String comment) {
        InfoSystem infoSystem = infoSystemService.getByIssueId(issueId);
        Issue issue = issueService.getIssueById(issueId);

        Set<String> issueParticipantsEmails = getIssueParticipantsEmails(issueId);
        Set<String> riaApproversEmails = userService.getApproversEmailsByOrganization(SecurityContextUtil.RIA_ORGANIZATION_CODE);
        issueParticipantsEmails.removeIf(email -> !riaApproversEmails.contains(email));

        issueParticipantsEmails.addAll(infoSystem.getContactsEmails());
        if (issueParticipantsEmails.isEmpty()) {
            log.info("New issue comment has been recently added for issue with id {}, "
                            + "but none of issue participants or info system contacts have emails.", issueId);
            return null;
        }
        String organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"))
                .getName();

        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(getDefaultNotificationRecipient(infoSystem.getShortName()));
        notificationModel.setBcc(issueParticipantsEmails.toArray(new String[issueParticipantsEmails.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setBaseUrl(getBaseUrl());
        notificationModel.setIssueId(issueId);
        notificationModel.setIssueTitle(issue.getTitle());
        notificationModel.setComment(comment);
        notificationModel.setAuthorName(organization);
        return notificationModel;
    }

    private Set<String> getIssueParticipantsEmails(Long issueId) {
        Set<String> participantsPersonalCodes = issueService.getParticipantsPersonalCodes(issueId);
        return userService.getEmailsByPersonalCodes(participantsPersonalCodes);
    }

    public void sendNewIssueToSystemContactsNotification(InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        List<String> to = infoSystem.getContactsEmails();
        if (to.isEmpty()) {
            log.info("New issue has been recently added, but info system '{}' has no contacts.",
                    infoSystem.getFullName());
            return;
        }

        NewIssueToSystemContactsEmailNotification notificationModel = new NewIssueToSystemContactsEmailNotification();
        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(getDefaultNotificationRecipient(infoSystem.getShortName()));
        notificationModel.setBcc(to.toArray(new String[to.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendIssueStatusUpdateNotification(Issue issue, boolean commented) {
        if (!isIssueStatusUpdateNotificationEnabled()) {
            log.info("Issue status update notifications sending is disabled");
            return;
        }

        IssueStatusUpdateNotification notificationModel = new IssueStatusUpdateNotification();

        InfoSystem infoSystem = infoSystemService.get(issue.getInfoSystemUuid());
        Set<String> participantsEmails = getIssueParticipantsEmails(issue.getId());
        participantsEmails.addAll(infoSystem.getContactsEmails());

        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(getDefaultNotificationRecipient(infoSystem.getShortName()));
        notificationModel.setBcc(participantsEmails.toArray(new String[0]));

        notificationModel.setIssue(IssueDataModel.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .status(issue.getStatus().name())
                .build());

        notificationModel.setInfoSystem(InfoSystemDataModel.builder()
                .fullName(infoSystem.getFullName())
                .shortName(infoSystem.getShortName())
                .build());

        notificationModel.setCommented(commented);
        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    public void sendNewIssueToApproversNotification(Issue issue, InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        Set<String> approversEmails = userService.getApproversEmailsByOrganization(SecurityContextUtil.RIA_ORGANIZATION_CODE);

        NewIssueToApproversEmailNotification notificationModel = new NewIssueToApproversEmailNotification();
        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(getDefaultNotificationRecipient(infoSystem.getShortName()));
        notificationModel.setBcc(approversEmails.toArray(new String[approversEmails.size()]));
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());

        notificationModel.setIssue(IssueDataModel.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .type(issue.getType() != null ? issue.getType().name() : null)
                .build());

        notificationModel.setBaseUrl(getBaseUrl());

        emailNotificationSenderService.sendNotification(notificationModel);
    }

    private String[] getDefaultNotificationRecipient(String infoSystemShortName) {
        String defaultNotificationRecipientPattern = applicationProperties.getNotification().getRecipientPattern();
        return new String[]{String.format(defaultNotificationRecipientPattern, infoSystemShortName)};
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

    private boolean isNewIssueDecisionNotificationEnabled() {
        return applicationProperties.getNotification().getNewIssueDecision().isEnabled();
    }

    private boolean isIssueStatusUpdateNotificationEnabled() {
        return applicationProperties.getNotification().getIssueStatusUpdate().isEnabled();
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
