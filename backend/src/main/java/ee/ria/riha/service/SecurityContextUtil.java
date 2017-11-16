package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
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

    private SecurityContextUtil() {
    }

    /**
     * Retrieves optional {@link RihaOrganization} from security context authentication. Active organization is not
     * present when security context holds no authentication, or authentication is not of {@link
     * RihaOrganizationAwareAuthenticationToken} type, or active organization is not set.
     *
     * @return optional of {@link RihaOrganization}
     */
    public static Optional<RihaOrganization> getActiveOrganization() {
        return getRihaAuthentication()
                .map(RihaOrganizationAwareAuthenticationToken::getActiveOrganization);
    }

    /**
     * Retrieves optional {@link RihaOrganizationAwareAuthenticationToken} from security context. Authentication is not
     * present when security context holds no authentication or authentication is not an instance of {@link
     * RihaOrganizationAwareAuthenticationToken}
     *
     * @return optional of {@link RihaOrganizationAwareAuthenticationToken}
     */
    public static Optional<RihaOrganizationAwareAuthenticationToken> getRihaAuthentication() {
        return getAuthentication()
                .map(authentication -> {
                    if (authentication instanceof RihaOrganizationAwareAuthenticationToken) {
                        return (RihaOrganizationAwareAuthenticationToken) authentication;
                    }

                    return null;
                });
    }

    /**
     * Retrieves optional {@link Authentication} from security context.
     *
     * @return optional of {@link Authentication}
     */
    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
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
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    if (principal instanceof UserDetails) {
                        return (UserDetails) principal;
                    }

                    return null;
                });
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
                .map(Authentication::getAuthorities)
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
