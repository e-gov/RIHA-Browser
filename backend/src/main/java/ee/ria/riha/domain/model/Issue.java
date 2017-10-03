package ee.ria.riha.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import static ee.ria.riha.domain.model.IssueEntityType.ISSUE;

/**
 * Issue entity model
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Issue implements IssueEntity {

    private Long id;
    private UUID infoSystemUuid;
    private Date dateCreated;
    private String title;
    private String comment;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;
    private IssueStatus status;

    public Issue() {
    }

    @Override
    public IssueEntityType getEntityType() {
        return ISSUE;
    }
}
