package ee.ria.riha.service.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class InfoSystemDataModel {

    private String fullName;
    private String shortName;

    public InfoSystemDataModel() {

    }
}
