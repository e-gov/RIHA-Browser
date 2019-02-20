package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.service.auth.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Valentin Suhnjov
 */
public class SecurityContextUtil {

    static final String RIA_ORGANIZATION_CODE = "70006317";

    private SecurityContextUtil() {
    }

    /**
     * Retrieves optional {@link RihaUserDetails} from security context. User details are not present when security
     * context holds no authentication or authenticated principal is not an instance of {@link RihaUserDetails}.
     *
     * @return optional of {@link RihaUserDetails}
     */
    public static Optional<RihaUserDetails> getRihaUserDetails() {
        return getUserDetails()
                .map(userDetails -> (RihaUserDetails) userDetails);
    }

    /**
     * Retrieves optional {@link UserDetails} from security context. User details are not present when security context
     * holds no authentication or authenticated principal is not an instance of {@link UserDetails}.
     *
     * @return optional of {@link UserDetails}
     */
    public static Optional<UserDetails> getUserDetails() {
        return getAuthentication().map(rihaUserDetails -> rihaUserDetails);
    }

    /**
     * Retrieves optional {@link Authentication} from security context.
     *
     * @return optional of {@link Authentication}
     */
    public static Optional<RihaUserDetails> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !(authentication.getPrincipal() instanceof RihaUserDetails)
                ? Optional.empty()
                : Optional.ofNullable( (RihaUserDetails) authentication.getPrincipal());
    }

    /**
     * Checks if current user is {@link RoleType#APPROVER} and also belongs to RIA organization with code {@link
     * #RIA_ORGANIZATION_CODE}.
     *
     * @return true in case user is RIA approver, false otherwise
     */
    public static boolean isRiaApprover() {
        return getActiveOrganization()
                .map(org -> RIA_ORGANIZATION_CODE.equals(org.getCode()) && hasRole(RoleType.APPROVER))
                .orElse(false);
    }

    /**
     * Retrieves optional {@link RihaOrganization} from security context authentication. Active organization is not
     * present when security context holds no authentication or active organization is not set.
     *
     * @return optional of {@link RihaOrganization}
     */
    public static Optional<RihaOrganization> getActiveOrganization() {
        return getAuthentication().map(RihaUserDetails::getActiveOrganization);
    }

    /**
     * Checks list of authentication authorities for specified role.
     *
     * @param role role to check
     * @return true in case user is in role, false otherwise
     */
    public static boolean hasRole(RoleType role) {
        return hasRole(role.getRole());
    }

    /**
     * Checks list of authentication authorities for specified role.
     *
     * @param role role to check
     * @return true in case user is in role, false when provided role has no text or user has no such role
     */
    public static boolean hasRole(String role) {
        if (!StringUtils.hasText(role)) {
            return false;
        }

        return getAuthentication()
                .map(RihaUserDetails::getAuthorities)
                .map(authorities -> authorities.contains(new SimpleGrantedAuthority(role)))
                .orElse(false);
    }

    /**
     * User is authenticated in case security context holds instance of {@link UserDetails}.
     *
     * @return true in case security context holds correct {@link UserDetails}, false otherwise
     */
    public static boolean isUserAuthenticated() {
        return getUserDetails()
                .isPresent();
    }

}
