package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Reduced model of {@link ee.ria.riha.domain.model.Issue} containing only essential data without any private details.
 */
@Data
public class IssueSummaryModel {

    private Long id;
    private UUID infoSystemUuid;
    private Date dateCreated;
    private String title;
    private String organizationName;
    private String organizationCode;
    private IssueStatus status;

}
