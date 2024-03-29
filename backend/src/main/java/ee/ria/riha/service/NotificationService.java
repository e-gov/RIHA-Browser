package ee.ria.riha.service;

import ee.ria.riha.conf.*;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.service.notification.*;
import ee.ria.riha.service.notification.model.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;

import static ee.ria.riha.service.SecurityContextUtil.*;

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

        Set<String> issueParticipantEmail = getIssueParticipantEmail(issueComment.getIssueId());

        issueParticipantEmail.forEach(mail ->{
            NewIssueCommentEmailNotification notificationModel = createIssueCommentNotification(
                    new NewIssueCommentEmailNotification(),
                    issueComment.getIssueId(),
                    issueComment.getComment(),
                    mail);

            emailNotificationSenderService.sendNotification(notificationModel);
        });
    }

    public void sendNewIssueDecisionNotification(IssueEvent decisionEvent) {
        if (!isNewIssueDecisionNotificationEnabled()) {
            log.info("New issue decision notifications sending is disabled.");
            return;
        }

        Set<String> issueParticipantEmail = getIssueParticipantEmail(decisionEvent.getIssueId());

        issueParticipantEmail.forEach(mail -> {
            NewIssueDecisionEmailNotification notificationModel = createIssueCommentNotification(
                    new NewIssueDecisionEmailNotification(),
                    decisionEvent.getIssueId(),
                    decisionEvent.getComment(),
                    mail);
            if (notificationModel != null) {
                notificationModel.setDecision(decisionEvent.getResolutionType());
                emailNotificationSenderService.sendNotification(notificationModel);
            }
        });
    }

    public Set<String> getIssueParticipantEmail(Long issueId){
        Set<String> issueParticipantsEmails = getIssueParticipantsEmails(issueId);
        Set<String> riaApproversEmails = userService.getApproversEmailsByOrganization(SecurityContextUtil.RIA_ORGANIZATION_CODE);

        issueParticipantsEmails.removeIf(email -> !riaApproversEmails.contains(email));
        issueParticipantsEmails.addAll(infoSystemService.getByIssueId(issueId).getContactsEmails());

        return issueParticipantsEmails;
    }

    private <T extends NewIssueCommentEmailNotification> T createIssueCommentNotification(T notificationModel,
                                                                                          Long issueId,
                                                                                          String comment,
                                                                                          String mail) {
        InfoSystem infoSystem = infoSystemService.getByIssueId(issueId);
        Issue issue = issueService.getIssueById(issueId);

        if (mail.isEmpty()) {
            log.info("New issue comment has been recently added for issue with id {}, "
                            + "but none of issue participants or info system contacts have emails.", issueId);
            return null;
        }
        String organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"))
                .getName();

        notificationModel.setFrom(getDefaultNotificationSender());
        notificationModel.setTo(mail);
        notificationModel.setInfoSystemFullName(infoSystem.getFullName());
        notificationModel.setInfoSystemShortName(infoSystem.getShortName());
        notificationModel.setBaseUrl(getBaseUrl());
        notificationModel.setIssueId(issueId);
        notificationModel.setIssueTitle(issue.getTitle());
        notificationModel.setComment(comment);
        notificationModel.setAuthorName(organization);
        notificationModel.setInfoSystemUuid(infoSystem.getUuid());
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

        List<String> sendTo = infoSystem.getContactsEmails();
        if (sendTo.isEmpty()) {
            log.info("New issue has been recently added, but info system '{}' has no contacts.",
                    infoSystem.getFullName());
            return;
        }

        sendTo.forEach(mail -> {
            NewIssueToSystemContactsEmailNotification notificationModel = new NewIssueToSystemContactsEmailNotification();
            notificationModel.setFrom(getDefaultNotificationSender());
            notificationModel.setInfoSystemFullName(infoSystem.getFullName());
            notificationModel.setInfoSystemShortName(infoSystem.getShortName());
            notificationModel.setInfoSystemUuid(infoSystem.getUuid());
            notificationModel.setBaseUrl(getBaseUrl());
            notificationModel.setTo(mail);
            emailNotificationSenderService.sendNotification(notificationModel);
        });
    }

    public void sendIssueStatusUpdateNotification(Issue issue, boolean commented) {
        if (!isIssueStatusUpdateNotificationEnabled()) {
            log.info("Issue status update notifications sending is disabled");
            return;
        }

        InfoSystem infoSystem = infoSystemService.get(issue.getInfoSystemUuid());
        Set<String> participantsEmails = new HashSet<>(infoSystem.getContactsEmails());

        participantsEmails.forEach(mail -> {
            IssueStatusUpdateNotification notificationModel = new IssueStatusUpdateNotification();
            notificationModel.setFrom(getDefaultNotificationSender());
            notificationModel.setTo(mail);

            notificationModel.setIssue(IssueDataModel.builder()
                    .id(issue.getId())
                    .title(issue.getTitle())
                    .status(issue.getStatus().name())
                    .build());

            notificationModel.setInfoSystem(InfoSystemDataModel.builder()
                    .fullName(infoSystem.getFullName())
                    .shortName(infoSystem.getShortName())
                    .uuid(infoSystem.getUuid())
                    .build());

            notificationModel.setCommented(commented);
            notificationModel.setBaseUrl(getBaseUrl());

            emailNotificationSenderService.sendNotification(notificationModel);
        });
    }

    public void sendNewIssueToApproversNotification(Issue issue, InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.info("New issue notifications sending is disabled.");
            return;
        }

        Set<String> approversEmails = userService.getApproversEmailsByOrganization(null);

        approversEmails.forEach(mail -> {
            NewIssueToApproversEmailNotification notificationModel = new NewIssueToApproversEmailNotification();
            notificationModel.setFrom(getDefaultNotificationSender());
            notificationModel.setTo(mail);
            notificationModel.setInfoSystemFullName(infoSystem.getFullName());
            notificationModel.setInfoSystemShortName(infoSystem.getShortName());
            notificationModel.setInfoSystemUuid(infoSystem.getUuid());

            notificationModel.setIssue(IssueDataModel.builder()
                    .id(issue.getId())
                    .title(issue.getTitle())
                    .type(issue.getType() != null ? issue.getType().name() : null)
                    .build());

            notificationModel.setBaseUrl(getBaseUrl());

            emailNotificationSenderService.sendNotification(notificationModel);
        });
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
    public void setIssueService(@Lazy IssueService issueService) {
        this.issueService = issueService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setInfoSystemService(@Lazy InfoSystemService infoSystemService) {
        this.infoSystemService = infoSystemService;
    }

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
}
