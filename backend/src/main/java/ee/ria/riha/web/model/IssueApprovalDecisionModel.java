package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueResolutionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of an issue approval decision request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueApprovalDecisionModel {
    private String comment;
    private IssueResolutionType decisionType;
}
