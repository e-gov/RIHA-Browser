package ee.ria.riha.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Model of an issue comment request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
@AllArgsConstructor
public class IssueCommentModel {
    private String comment;
}
