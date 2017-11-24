package ee.ria.riha.web;

import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.service.IssueService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwnerOrReviewer;
import ee.ria.riha.service.auth.PreAuthorizeIssueOwnerOrReviewer;
import ee.ria.riha.storage.util.*;
import ee.ria.riha.web.model.IssueSummaryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * Retrieve paginated and filtered list of issues for given info system.
     *
     * @param shortName  a short name of info system
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of info system issues
     */
    @GetMapping(API_V1_PREFIX + "/systems/{shortName}/issues")
    @ApiOperation("List all issues of information system")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<IssueSummaryModel>> listInfoSystemIssues(@PathVariable("shortName") String shortName,
                                                                                 Pageable pageable, Filterable filterable) {
        PagedResponse<Issue> issues = issueService.listInfoSystemIssues(shortName, pageable, filterable);

        return ResponseEntity.ok(
                new PagedResponse<>(new PageRequest(issues.getPage(), issues.getSize()),
                        issues.getTotalElements(),
                        issues.getContent().stream()
                                .map(issueSummaryModelMapper::map)
                                .collect(toList())));
    }

    /**
     * Adds single issue to the info system.
     *
     * @param shortName a short name of info system
     * @param model     issue model
     * @return created issue
     */
    @PostMapping(API_V1_PREFIX + "/systems/{shortName}/issues")
    @PreAuthorizeInfoSystemOwnerOrReviewer
    @ApiOperation("Create new issue for information system")
    public ResponseEntity<Issue> createInfoSystemIssue(@PathVariable("shortName") String shortName,
                                                       @RequestBody Issue model) {
        return ResponseEntity.ok(issueService.createInfoSystemIssue(shortName, model));
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
    public ResponseEntity<Issue> updateStatus(@PathVariable("issueId") Long issueId, @RequestBody Issue model) {
        return ResponseEntity.ok(issueService.updateIssueStatus(issueId, model.getStatus(), model.getComment()));
    }

}