package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Special model of {@link ee.ria.riha.domain.model.Issue} that is used to provide issues to
 * approvers dashboard.
 */
@Getter
@Setter
@Builder
public class DashboardIssue {

    private Long id;
    private Date dateCreated;
    private IssueType type;
    private String title;
    private String infoSystemFullName;
    private String infoSystemShortName;
    private String authorName;
    private String organizationName;
    private DashboardIssueComment lastComment;
}
