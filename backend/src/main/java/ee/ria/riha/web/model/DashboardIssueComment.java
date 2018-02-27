package ee.ria.riha.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Special model of {@link ee.ria.riha.domain.model.IssueComment} that represents last
 * dashboard issue comment data.
 */
@Getter
@Setter
@Builder
public class DashboardIssueComment {

    private Long id;
    private Date dateCreated;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;
}
