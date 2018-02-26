package ee.ria.riha.web;

import ee.ria.riha.service.DashboardService;
import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.DashboardIssue;
import ee.ria.riha.web.model.DashboardIssueRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/dashboard")
public class DashboardController {

    private static final String FILTER_PARAMETER = "filter";
    private static final String SORT_PARAMETER = "sort";

    private DashboardService dashboardService;

    @GetMapping("/issues/my")
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserRelatedIssues(Pageable pageable,
           @RequestParam MultiValueMap<String, String> requestParameters) {

        return ResponseEntity.ok(dashboardService.listIssues(DashboardIssueRequestType.USER_RELATED,
                createFilterRequest(requestParameters), pageable));
    }

    @GetMapping("/issues/org")
    public ResponseEntity<PagedResponse<DashboardIssue>> listUserActiveOrganizationRelatedIssues(Pageable pageable,
           @RequestParam MultiValueMap<String, String> requestParameters) {

        return ResponseEntity.ok(dashboardService.listIssues(DashboardIssueRequestType.USER_ACTIVE_ORGANIZATION_RELATED,
                createFilterRequest(requestParameters), pageable));
    }

    private CompositeFilterRequest createFilterRequest(MultiValueMap<String, String> requestParameters) {
        CompositeFilterRequest filterRequest = new CompositeFilterRequest();

        if (requestParameters == null || requestParameters.isEmpty()) {
            return filterRequest;
        }

        List<String> filterParameters = requestParameters.get(FILTER_PARAMETER);
        List<String> sortParameters = requestParameters.get(SORT_PARAMETER);

        if (filterParameters != null && !filterParameters.isEmpty()) {
            filterRequest.addFilterParameters(filterParameters);
        }

        if (sortParameters != null && !sortParameters.isEmpty()) {
            filterRequest.addSortParameters(sortParameters);
        }

        return filterRequest;
    }

    @Autowired
    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
}
