package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import lombok.*;

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
@NoArgsConstructor
public class RelationSummaryModel {
    private Long id;
    private UUID infoSystemUuid;
    private String infoSystemName;
    private String infoSystemShortName;
    private RelationType type;
}
