package ee.ria.riha.service;

import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.DashboardIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;

@Service
public class DashboardService {

    private static final String PROPERTY_AUTHOR_PERSONAL_CODE = "author_personal_code";
    private static final String PROPERTY_ORGANIZATION_CODE = "organization_code";
    private static final String ACTION_AUTHOR_OR_ORGANIZATION_CODE = "author-or-organization-code";

    private IssueService issueService;

    public PagedResponse<DashboardIssue> listIssuesMentioningUser(CompositeFilterRequest filter, Pageable pageable) {
        String personalCode = getRihaUserDetails()
                .orElseThrow(() -> new IllegalBrowserStateException("User details not present in security context"))
                .getPersonalCode();

        filter.addFilterParameter(ACTION_AUTHOR_OR_ORGANIZATION_CODE);
        filter.addFilterParameter(PROPERTY_AUTHOR_PERSONAL_CODE + ":" + personalCode);

        return issueService.listDashboardIssues(pageable, filter);
    }

    public PagedResponse<DashboardIssue> listIssuesMentioningOrganization(CompositeFilterRequest filter,
                                                                          Pageable pageable) {
        String organizationCode = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"))
                .getCode();

        filter.addFilterParameter(ACTION_AUTHOR_OR_ORGANIZATION_CODE);
        filter.addFilterParameter(PROPERTY_ORGANIZATION_CODE + ":" + organizationCode);

        return issueService.listDashboardIssues(pageable, filter);
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}
