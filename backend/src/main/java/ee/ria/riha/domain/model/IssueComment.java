package ee.ria.riha.domain.model;

import lombok.*;

import java.util.Date;

import static ee.ria.riha.domain.model.IssueEntityType.ISSUE_COMMENT;

/**
 * Issue comment entity model
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueComment implements IssueEntity {

    private Long id;
    private Long issueId;
    private String comment;
    private Date dateCreated;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;

    @Override
    public IssueEntityType getEntityType() {
        return ISSUE_COMMENT;
    }
}
