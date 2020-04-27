package ee.ria.riha.service.notification.model;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

/**
 * Notification model reflecting issue update event.
 */
@Getter
@Setter
public class IssueStatusUpdateNotification extends SimpleHtmlEmailNotification {

    /**
     * Info system data that this issue is related to
     */
    private InfoSystemDataModel infoSystem;

    /**
     * Updated issue data
     */
    private IssueDataModel issue;

    /**
     * Indicates if issue was commented during status change
     */
    private boolean commented;

    /**
     * Application base URL for reference
     */
    private String baseUrl;
  
    /**
     * Info system uuid that this issue is related to
     */
    private UUID infoSystemUuid;


}
