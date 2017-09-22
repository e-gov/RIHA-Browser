package ee.ria.riha.web;

import ee.ria.riha.domain.model.IssueComment;
import ee.ria.riha.service.IssueCommentService;
import ee.ria.riha.storage.util.ApiPageableAndFilterableParams;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * Issue comments.
 *
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/issues")
@Secured("ROLE_RIHA_USER")
@Api("Issue events")
public class IssueCommentController {

    @Autowired
    private IssueCommentService issueEventService;

    /**
     * Retrieve paginated and filtered list of issue comments.
     *
     * @param issueId    issue id
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paginated list of issue comments
     */
    @GetMapping("/{issueId}/comments")
    @ApiOperation("List all issue comments")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<IssueComment>> listIssueComments(
            @PathVariable("issueId") Long issueId,
            Pageable pageable,
            Filterable filterable) {
        return ResponseEntity.ok(
                issueEventService.listIssueComments(issueId, pageable, filterable));
    }

    /**
     * Get single comment by its id.
     *
     * @param issueId   an id of an issue
     * @param commentId comment id
     * @return single concrete comment or null
     */
    @GetMapping("/{issueId}/comments/{commentId}")
    @ApiOperation("Get single issue comment")
    public ResponseEntity<IssueComment> getIssueComment(
            @PathVariable("issueId") Long issueId,
            @PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(
                issueEventService.getIssueCommentById(commentId));
    }

    /**
     * Adds single comment to the issue.
     *
     * @param issueId      an id of an issue
     * @param issueComment comment model
     * @return created issue comment
     */
    @PostMapping("/{issueId}/comments")
    @ApiOperation("Create new issue comment")
    public ResponseEntity<IssueComment> createIssueComment(
            @PathVariable("issueId") Long issueId,
            @RequestBody IssueComment issueComment) {
        return ResponseEntity.ok(
                issueEventService.createIssueComment(issueId, issueComment));
    }

}
