package ee.ria.riha.service.notifications.model;

import lombok.*;

/**
 * Simplified info system data model that is used for communication with notification service.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfoSystemDataModel {

    private String fullName;
    private String shortName;

}
