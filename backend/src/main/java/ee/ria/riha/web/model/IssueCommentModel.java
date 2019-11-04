package ee.ria.riha.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of an issue comment request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueCommentModel {
    private String comment;
}
