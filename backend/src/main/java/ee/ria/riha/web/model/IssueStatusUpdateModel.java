package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueResolutionType;
import ee.ria.riha.domain.model.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of an issue status update request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueStatusUpdateModel {
    private String comment;
    private IssueStatus status;
    private IssueResolutionType resolutionType;
}
