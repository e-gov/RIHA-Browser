package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import java.util.UUID;
import lombok.*;

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
