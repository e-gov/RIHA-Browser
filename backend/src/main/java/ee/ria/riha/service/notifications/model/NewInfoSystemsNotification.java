package ee.ria.riha.service.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Holds data for new info systems notification messages.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewInfoSystemsNotification implements NotificationDataModel {

    private List<InfoSystemDataModel> infoSystems;
}
