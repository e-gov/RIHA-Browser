package ee.ria.riha.service.notifications.model;

import lombok.Builder;
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

    @Builder
    public NewInfoSystemsEmailNotification(String from, String[] to, String subject, String[] cc, String[] bcc,
                                           List<InfoSystemDataModel> infoSystems, String baseUrl) {
        super(from, to, subject, cc, bcc);
        this.infoSystems = infoSystems;
        this.baseUrl = baseUrl;
    }
}
