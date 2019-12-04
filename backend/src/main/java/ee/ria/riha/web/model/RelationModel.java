package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import lombok.*;

/**
 * Model of info system relationship request.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationModel {
    private Long id;
    private String infoSystemShortName;
    private RelationType type;
}
