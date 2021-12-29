package ee.ria.riha.domain.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationResponse {
    private Long id;
    private Date creationDate;
    private Date modifiedDate;

    private UUID infoSystemUuid;
    private String infoSystemName;
    private String infoSystemShortName;
    private String infoSystemStatus;

    private UUID relatedInfoSystemUuid;
    private String relatedInfoSystemName;
    private String relatedInfoSystemShortName;

    private RelationType type;

    private boolean reversed;


    public RelationResponse reverse() {
        return RelationResponse.builder()
                .id(this.id)
                .creationDate(this.creationDate)
                .modifiedDate(this.modifiedDate)
                .infoSystemUuid(this.relatedInfoSystemUuid)
                .infoSystemName(this.relatedInfoSystemName)
                .infoSystemShortName(this.relatedInfoSystemShortName)
                .infoSystemStatus(this.infoSystemStatus)
                .relatedInfoSystemUuid(this.infoSystemUuid)
                .relatedInfoSystemName(this.infoSystemName)
                .relatedInfoSystemShortName(this.infoSystemShortName)
                .type(this.type != null ? this.type.getOpposite() : null)
                .reversed(!this.reversed)
                .build();
    }
}

