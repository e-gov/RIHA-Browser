package ee.ria.riha.domain.model;

import static ee.ria.riha.domain.model.IssueEntityType.ISSUE_EVENT;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
  private String comment;
  @NonNull private IssueEventType type;
  private Date dateCreated;
  private String authorName;
  private String authorPersonalCode;
  private String organizationName;
  private String organizationCode;
  private IssueResolutionType resolutionType;

  @Override
  public IssueEntityType getEntityType() {
    return ISSUE_EVENT;
  }
}
