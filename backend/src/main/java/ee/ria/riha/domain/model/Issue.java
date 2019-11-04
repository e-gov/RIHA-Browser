package ee.ria.riha.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static ee.ria.riha.domain.model.IssueEntityType.ISSUE;

/**
 * Issue entity model
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Issue implements IssueEntity {

    private Long id;
    private UUID infoSystemUuid;
    private Date dateCreated;
    private String title;
    private String comment;
    private String authorName;
    private String authorPersonalCode;
    private String organizationName;
    private String organizationCode;
    private IssueStatus status;
    private IssueType type;
    private IssueResolutionType resolutionType;
    private LocalDate decisionDeadline;

    @Override
    public IssueEntityType getEntityType() {
        return ISSUE;
    }
}
