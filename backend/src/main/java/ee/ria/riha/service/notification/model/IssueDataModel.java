package ee.ria.riha.service.notification.model;

import lombok.*;

/**
 * Simplified issue model that is used for communication with notification service.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IssueDataModel {

    private String title;
    private String status;

}
