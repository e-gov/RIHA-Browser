package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Model of info system relationship response.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RelationSummaryModel {
    private Long id;
    private UUID infoSystemUuid;
    private String infoSystemName;
    private String infoSystemShortName;
    private RelationType type;
}
