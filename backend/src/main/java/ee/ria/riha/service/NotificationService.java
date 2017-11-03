package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.notifications.EmailNotificationSenderService;
import ee.ria.riha.service.notifications.model.NewIssueNotification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class NotificationService {

    private final boolean newIssueNotificationEnabled;

    private EmailNotificationSenderService emailNotificationSenderService;

    @Autowired
    public NotificationService(ApplicationProperties applicationProperties) {
        newIssueNotificationEnabled = applicationProperties.getNotification().getNewIssue().isEnabled();
    }

    public void sendNewIssueNotification(InfoSystem infoSystem) {
        if (!isNewIssueNotificationEnabled()) {
            log.warn("New issue notifications sending is disabled.");
            return;
        }

        String[] to = getSystemContacts(infoSystem);
        if (to.length == 0) {
            log.info("New issue has been recently added, but info system '{}' has no contacts.", infoSystem.getFullName());
            return;
        }

        NewIssueNotification model = NewIssueNotification.builder()
                .infoSystemFullName(infoSystem.getFullName())
                .infoSystemShortName(infoSystem.getShortName())
                .to(to)
                .build();

        emailNotificationSenderService.sendNotification(model);
    }

    private String[] getSystemContacts(InfoSystem infoSystem) {
        JSONArray jsonContactsArray = infoSystem.getJsonObject().optJSONArray("contacts");
        int jsonContactsArrayLength = jsonContactsArray.length();
        if (jsonContactsArrayLength == 0) {
            return new String[0];
        }

        String[] contacts = new String[jsonContactsArrayLength];
        for (int i = 0; i < jsonContactsArrayLength; i++) {
            contacts[i] = jsonContactsArray.optJSONObject(i).optString("email");
        }
        return contacts;
    }

    @Autowired
    public void setEmailNotificationSenderService(EmailNotificationSenderService emailNotificationSenderService) {
        this.emailNotificationSenderService = emailNotificationSenderService;
    }
}
