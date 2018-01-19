package ee.ria.riha.web.model;

import lombok.Builder;
import lombok.Data;

/**
 * Model of an issue comment request
 *
 * @author Valentin Suhnjov
 */
@Data
@Builder
public class IssueCommentModel {

    private String comment;

}
