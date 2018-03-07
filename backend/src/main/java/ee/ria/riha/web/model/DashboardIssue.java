package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Special model of {@link ee.ria.riha.domain.model.Issue} that is used to provide issues to
 * approvers dashboard.
 */
@Getter
@Setter
@Builder
public class DashboardIssue {

    private Long id;
    private IssueType type;
    private String title;
    private String infoSystemFullName;
    private String infoSystemShortName;
    private DashboardIssueComment lastComment;
}
