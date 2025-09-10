package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.RelationType;
import java.util.UUID;
import lombok.*;

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
