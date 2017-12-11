package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Holds data for new info systems email notification messages.
 */
@Getter
@Setter
public class NewInfoSystemsEmailNotification extends SimpleHtmlEmailNotification {

    private List<InfoSystemDataModel> infoSystems;
    private String baseUrl;
}
