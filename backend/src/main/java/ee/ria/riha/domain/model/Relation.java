package ee.ria.riha.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Relation {

    private Long id;
    private Date creationDate;
    private Date modifiedDate;

    private UUID infoSystemUuid;
    private String infoSystemName;
    private String infoSystemShortName;

    private UUID relatedInfoSystemUuid;
    private String relatedInfoSystemName;
    private String relatedInfoSystemShortName;

    private RelationType type;

    private boolean reversed;

    public Relation() {
    }

    public Relation reverse() {
        return Relation.builder()
                .id(this.id)
                .creationDate(this.creationDate)
                .modifiedDate(this.modifiedDate)
                .infoSystemUuid(this.relatedInfoSystemUuid)
                .infoSystemName(this.relatedInfoSystemName)
                .infoSystemShortName(this.relatedInfoSystemShortName)
                .relatedInfoSystemUuid(this.infoSystemUuid)
                .relatedInfoSystemName(this.infoSystemName)
                .relatedInfoSystemShortName(this.infoSystemShortName)
                .type(this.type != null ? this.type.getOpposite() : null)
                .reversed(!this.reversed)
                .build();
    }
}
