package ee.ria.riha.service;

import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.DashboardIssue;
import ee.ria.riha.web.model.DashboardIssueRequestType;
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

    public PagedResponse<DashboardIssue> listIssues(DashboardIssueRequestType requestType,
                                                    CompositeFilterRequest filter, Pageable pageable) {

        filter.addFilterParameter(ACTION_AUTHOR_OR_ORGANIZATION_CODE);
        filter.addFilterParameter(preparePropertyFilterParameter(requestType));

        return issueService.listDashboardIssues(pageable, filter);
    }

    private String preparePropertyFilterParameter(DashboardIssueRequestType requestType) {
        switch (requestType) {
            case USER_RELATED: return String.format("%s:%s", PROPERTY_AUTHOR_PERSONAL_CODE, getRihaUserDetails()
                    .orElseThrow(() -> new ValidationException("validation.dashboard.issue.getUserRelated.userIsNotAuthenticated"))
                    .getPersonalCode());
            case USER_ACTIVE_ORGANIZATION_RELATED: return String.format("%s:%s", PROPERTY_ORGANIZATION_CODE, getActiveOrganization()
                    .orElseThrow(() -> new ValidationException("validation.dashboard.issue.getOrganizationRelated.activeOrganizationIsNotSet"))
                    .getCode());
            default: return null;
        }
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}
