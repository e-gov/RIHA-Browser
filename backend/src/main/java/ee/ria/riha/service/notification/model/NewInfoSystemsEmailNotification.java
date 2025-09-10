package ee.ria.riha.service.notification.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Holds data for new info systems email notification messages. */
@Getter
@Setter
public class NewInfoSystemsEmailNotification extends SimpleHtmlEmailNotification {

  private List<InfoSystemDataModel> infoSystems;
  private String baseUrl;
}
