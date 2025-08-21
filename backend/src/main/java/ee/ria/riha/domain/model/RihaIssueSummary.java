package ee.ria.riha.domain.model;

import ee.ria.riha.web.model.IssueEventSummaryModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Special model of {@link ee.ria.riha.domain.model.Issue} that is used to provide issues to
 * approvers.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RihaIssueSummary {

  private Long id;
  private String infoSystemShortName;
  private Date dateCreated;
  private String title;
  private IssueType issueType;
  private String organizationName;
  private String organizationCode;
  private IssueStatus status;
  private String infoSystemFullName;
  private IssueResolutionType resolutionType;
  private List<IssueEventSummaryModel> events;
  private LocalDate decisionDeadline;
}
