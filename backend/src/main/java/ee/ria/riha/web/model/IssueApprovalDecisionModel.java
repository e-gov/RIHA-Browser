package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueResolutionType;
import lombok.Builder;
import lombok.Data;

/**
 * Model of an issue approval decision request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
public class IssueApprovalDecisionModel {

    private String comment;
    private IssueResolutionType decisionType;

}
