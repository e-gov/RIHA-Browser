package ee.ria.riha.web;

import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.domain.model.RihaIssueSummary;
import ee.ria.riha.service.IssueService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwnerOrReviewer;
import ee.ria.riha.service.auth.PreAuthorizeIssueOwnerOrReviewer;
import ee.ria.riha.storage.util.*;
import ee.ria.riha.web.model.IssueApprovalDecisionModel;
import ee.ria.riha.web.model.IssueStatusUpdateModel;
import ee.ria.riha.web.model.IssueSummaryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;
import static java.util.stream.Collectors.toList;

/**
 * Info system issues
 */
@RestController
@Api("Issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueSummaryModelMapper issueSummaryModelMapper;

    /**
     * Retrieve paginated and filtered list of issues for info system referenced by either UUID or short name.
     *
     * @param reference  info system reference
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of info system issues
     */
    @GetMapping(API_V1_PREFIX + "/systems/{reference}/issues")
    @ApiOperation("List all issues of information system")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<IssueSummaryModel>> listInfoSystemIssues(
            @PathVariable("reference") String reference,
            Pageable pageable, Filterable filterable) {
        PagedResponse<Issue> issues = issueService.listInfoSystemIssues(reference, pageable, filterable);

        return ResponseEntity.ok(
                new PagedResponse<>(new PageRequest(issues.getPage(), issues.getSize()),
                        issues.getTotalElements(),
                        issues.getContent().stream()
                                .map(issueSummaryModelMapper::map)
                                .collect(toList())));
    }

    /**
     * Retrieve paginated and filtered list of all RIHA issues.
     *
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated and filtered list of all RIHA issues
     */
    @GetMapping(API_V1_PREFIX + "/issues")
    @ApiOperation("List all RIHA issues")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<RihaIssueSummary>> listIssues(Pageable pageable, Filterable filterable) {
        return ResponseEntity.ok(issueService.listIssues(pageable, filterable));
    }

    /**
     * Adds single issue to the info system referenced by either UUID or short name.
     *
     * @param reference info system reference
     * @param model     issue model
     * @return created issue
     */
    @PostMapping(API_V1_PREFIX + "/systems/{reference}/issues")
    @PreAuthorizeInfoSystemOwnerOrReviewer
    @ApiOperation("Create new issue for information system")
    public ResponseEntity<Issue> createInfoSystemIssue(@PathVariable("reference") String reference,
                                                       @RequestBody Issue model) {
        return ResponseEntity.ok(issueService.createInfoSystemIssue(reference, model));
    }

    /**
     * Retrieve single issue by id.
     *
     * @param issueId id of an issue
     * @return issue or null
     */
    @GetMapping(API_V1_PREFIX + "/issues/{issueId}")
    @PreAuthorizeIssueOwnerOrReviewer
    @ApiOperation("Get single information system issue")
    public ResponseEntity<Issue> getInfoSystemIssue(@PathVariable("issueId") Long issueId) {
        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    /**
     * Update issue.
     *
     * @param issueId id of an issue
     * @param model   updated issue model
     * @return updated issue
     */
    @PutMapping(API_V1_PREFIX + "/issues/{issueId}")
    @PreAuthorizeIssueOwnerOrReviewer
    @ApiOperation("Update issue")
    public ResponseEntity<Issue> updateStatus(@PathVariable("issueId") Long issueId, @RequestBody IssueStatusUpdateModel model) {
        return ResponseEntity.ok(issueService.updateIssueStatus(issueId, model));
    }

    /**
     * Make decision about approval request/
     * @param issueId id of an approval request issue
     * @param model approval decision model
     */
    @PostMapping(API_V1_PREFIX + "/issues/{issueId}/decisions")
    @PreAuthorize("hasRole('ROLE_HINDAJA')")
    @ApiOperation("Leave decision")
    public void makeApprovalDecision(@PathVariable("issueId") Long issueId, @RequestBody IssueApprovalDecisionModel model) {
        issueService.makeApprovalDecision(issueId, model);
    }

}