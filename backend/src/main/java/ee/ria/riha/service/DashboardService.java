package ee.ria.riha.service;

import ee.ria.riha.domain.model.IssueStatus;
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
    private static final String ACTION_ORGANIZATION_INFOSYSTEMS_RELATED_ISSUES = "organization-infosystems-issues";

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

    public PagedResponse<DashboardIssue> listOrganizationIssues(CompositeFilterRequest filter, Pageable pageable,
                                                                String organizationCode) {

        filter.addFilterParameter(ACTION_ORGANIZATION_INFOSYSTEMS_RELATED_ISSUES);
        filter.addFilterParameter(prepareOrganizationCodeFilterParameter(organizationCode));
        filter.addFilterParameter(prepareIssueStatusFilterParameter(IssueStatus.OPEN));

        return issueService.listDashboardIssues(pageable, filter);
    }


    private String prepareIssueStatusFilterParameter(IssueStatus issueStatus) {
        return "status:" + issueStatus;
    }

    private String prepareOrganizationCodeFilterParameter(String organizationCode) {
        return "organization_code:" + organizationCode;
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}
