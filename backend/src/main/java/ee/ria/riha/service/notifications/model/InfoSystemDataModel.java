package ee.ria.riha.service.notifications.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Simplified info system data model that is used to operate with FreeMarker templates.
 */
@Getter
@Setter
@Builder
public class InfoSystemDataModel {

    private String fullName;
    private String shortName;
}
