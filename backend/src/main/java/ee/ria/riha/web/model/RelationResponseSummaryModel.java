package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationResponseSummaryModel {
    private Long id;
    private UUID infoSystemUuid;
    private String infoSystemName;
    private String infoSystemShortName;
    private RelationType type;
    private String infoSystemStatus;
}
