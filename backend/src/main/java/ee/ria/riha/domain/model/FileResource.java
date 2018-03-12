package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
