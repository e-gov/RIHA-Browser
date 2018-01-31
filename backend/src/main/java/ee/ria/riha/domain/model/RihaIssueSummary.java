package ee.ria.riha.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Extra special model of {@link ee.ria.riha.domain.model.Issue} that adds info system short name info.
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
    private String organizationName;
    private String organizationCode;
    private IssueStatus status;
}
