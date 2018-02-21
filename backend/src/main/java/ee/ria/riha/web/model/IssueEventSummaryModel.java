package ee.ria.riha.web.model;

import ee.ria.riha.domain.model.IssueEventType;
import ee.ria.riha.domain.model.IssueResolutionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

/**
 * Special model of {@link ee.ria.riha.domain.model.IssueEvent} that is used to provide issue update events data to approvers.
 */
@Getter
@Setter
@Builder
public class IssueEventSummaryModel {

    private Long id;
    @NonNull
    private IssueEventType type;
    private String organizationName;
    private String organizationCode;
    private IssueResolutionType resolutionType;
    private Date dateCreated;
}
