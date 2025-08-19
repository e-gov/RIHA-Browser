package ee.ria.riha.domain.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
public class FileResource {

  private UUID fileResourceUuid;
  private String infoSystemShortName;
  private String infoSystemOwnerCode;
  private String infoSystemOwnerName;
  private UUID infoSystemUuid;
  private String fileResourceName;
  private String infoSystemName;
}
