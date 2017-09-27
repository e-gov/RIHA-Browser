package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

import static ee.ria.riha.domain.model.IssueEntityType.ISSUE_EVENT;

/**
 * Issue status update event
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
public class IssueEvent implements IssueEntity {

    private Long id;
    private Long issueId;
    @NonNull
    private IssueEventType type;
    private Date dateCreated;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;

    @Override
    public IssueEntityType getEntityType() {
        return ISSUE_EVENT;
    }
}
