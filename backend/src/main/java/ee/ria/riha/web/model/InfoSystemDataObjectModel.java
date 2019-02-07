package ee.ria.riha.web.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class InfoSystemDataObjectModel {

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
