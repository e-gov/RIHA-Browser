package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Model of info system relationship request.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RelationModel {
    private Long id;
    private String infoSystemShortName;
    private RelationType type;
}
