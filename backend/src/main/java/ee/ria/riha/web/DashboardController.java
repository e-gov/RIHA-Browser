package ee.ria.riha.web;

import ee.ria.riha.service.DashboardService;
import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.DashboardIssue;
import ee.ria.riha.web.model.DashboardIssueRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/issues/my")
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserRelatedIssues(Pageable pageable,
           CompositeFilterRequest filterRequest) {

        return ResponseEntity.ok(dashboardService.listIssues(DashboardIssueRequestType.USER_RELATED,
                filterRequest, pageable));
    }

    @GetMapping("/issues/org")
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserActiveOrganizationRelatedIssues(Pageable pageable,
           CompositeFilterRequest filterRequest) {

        return ResponseEntity.ok(dashboardService.listIssues(DashboardIssueRequestType.USER_ACTIVE_ORGANIZATION_RELATED,
                filterRequest, pageable));
    }

    @GetMapping("/organizationIssues/{organizationCode}")
    public ResponseEntity<PagedResponse<DashboardIssue>> listOrganizationInfoSystemsRelatedIssues(
            @PathVariable("organizationCode") String organizationCode, Pageable pageable,
            CompositeFilterRequest filterRequest) {

        return ResponseEntity.ok(dashboardService.listOrganizationIssues(filterRequest, pageable, organizationCode));
    }

    @Autowired
    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
}
