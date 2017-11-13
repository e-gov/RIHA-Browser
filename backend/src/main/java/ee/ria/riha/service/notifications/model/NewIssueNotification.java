package ee.ria.riha.service.notifications.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue notification messages.
 */
@Getter
@Setter
@Builder
public class NewIssueNotification implements NotificationDataModel {

    private String[] to;
    private String infoSystemFullName;
    private String infoSystemShortName;
}
