package ee.ria.riha.web.model;

public enum DashboardIssueRequestType {

    /**
     * Indicates that request was sent to retrieve issues where concrete user has been taken
     * part in.
     */
    USER_RELATED,

    /**
     * Indicates that request was sent to retrieve issues where concrete user's active
     * organization has been taken part in.
     */
    USER_ACTIVE_ORGANIZATION_RELATED
}
