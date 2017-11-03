package ee.ria.riha.service.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewIssueNotification implements NotificationDataModel {

    private String[] to;
    private String infoSystemFullName;
    private String infoSystemShortName;

    public NewIssueNotification() {

    }
}
