package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

import static ee.ria.riha.domain.model.EntityType.ISSUE_EVENT;

/**
 * Issue status update event
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
public class IssueEvent implements Entity {

    private Long id;
    private Long issueId;
    private @NonNull
    IssueEventType type;
    private Date dateCreated;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;

    @Override
    public EntityType getEntityType() {
        return ISSUE_EVENT;
    }
}
