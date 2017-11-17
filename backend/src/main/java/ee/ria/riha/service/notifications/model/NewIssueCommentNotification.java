package ee.ria.riha.service.notifications.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue comment notification messages.
 */
@Getter
@Setter
@Builder
public class NewIssueCommentNotification implements NotificationDataModel {

    private String[] to;
    private String infoSystemFullName;
    private String infoSystemShortName;
}
