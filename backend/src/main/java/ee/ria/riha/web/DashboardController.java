package ee.ria.riha.web;

import ee.ria.riha.service.DashboardService;
import ee.ria.riha.storage.util.ApiPageableAndCompositeRequestParams;
import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.DashboardIssue;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/issues/my")
    @PreAuthorize("hasRole('ROLE_RIHA_USER')")
    @ApiOperation("List issues related to current user")
    @ApiPageableAndCompositeRequestParams
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserRelatedIssues(Pageable pageable,
                                                                               CompositeFilterRequest filterRequest) {
        return ResponseEntity.ok(dashboardService.listIssuesMentioningUser(filterRequest, pageable));
    }

    @GetMapping("/issues/org")
    @PreAuthorize("hasRole('ROLE_RIHA_USER')")
    @ApiOperation("List issues related to current user organization")
    @ApiPageableAndCompositeRequestParams
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserActiveOrganizationRelatedIssues(Pageable pageable,
                                                                                                 CompositeFilterRequest filterRequest) {
        return ResponseEntity.ok(dashboardService.listIssuesMentioningOrganization(filterRequest, pageable));
    }

    @Autowired
    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
}
