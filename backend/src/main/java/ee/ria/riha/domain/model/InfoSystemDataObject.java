package ee.ria.riha.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InfoSystemDataObject {

    private String infosystem;
    private String dataObjectName;
    private String comment;
    private String parentObject;
    private String shortName;
    private UUID fileUuid;
    private Boolean diaFlag;
    private Boolean avFlag;
    private Boolean iaFlag;
    private Boolean paFlag;



}
