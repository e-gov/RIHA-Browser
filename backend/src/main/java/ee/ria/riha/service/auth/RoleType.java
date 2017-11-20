package ee.ria.riha.service.auth;

/**
 * @author Valentin Suhnjov
 */
public enum RoleType {

    /**
     * Any logged in user (not anonymous)
     */
    AUTHENTICATED_USER("ROLE_RIHA_USER"),

    /**
     * Hindaja user
     */
    APPROVER("ROLE_HINDAJA"),

    /**
     * Kirjeldaja user
     */
    PRODUCER("ROLE_KIRJELDAJA");

    private String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
