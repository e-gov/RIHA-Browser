package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Valentin Suhnjov
 */
public class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    /**
     * Retrieves {@link RihaOrganization} from security context authentication. Returns null in case security context
     * holds no authentication, or authentication is not of {@link RihaOrganizationAwareAuthenticationToken} type, or
     * active organization is not set.
     *
     * @return instance of {@link RihaOrganization} or null if active organization is not set
     */
    public static RihaOrganization getActiveOrganization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof RihaOrganizationAwareAuthenticationToken)) {
            return null;
        }

        return ((RihaOrganizationAwareAuthenticationToken) authentication).getActiveOrganization();
    }

    /**
     * Retrieves {@link RihaUserDetails} from security context. Returns null in case security context holds no
     * authentication or authenticated principal is not an instance of {@link RihaUserDetails}.
     *
     * @return instance of {@link RihaUserDetails} or null
     */
    public static RihaUserDetails getRihaUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof RihaUserDetails)) {
            return null;
        }

        return ((RihaUserDetails) principal);
    }

    /**
     * Retrieved personal code from {@link RihaUserDetails}.
     *
     * @return personal code or null in case context {@link UserDetails} is not of {@link RihaUserDetails} type
     */
    public static String getRihaUserPersonalCode() {
        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        return rihaUserDetails != null ? rihaUserDetails.getPersonalCode() : null;
    }

    /**
     * Retrieves {@link UserDetails} from security context. Returns null in case security context holds no
     * authentication or authenticated principal is not an instance of {@link UserDetails}.
     *
     * @return instance of {@link RihaUserDetails} or null
     */
    public static UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }

        return ((UserDetails) principal);
    }

    /**
     * User is authenticated in case security context holds instance of {@link UserDetails}.
     *
     * @return true in case security context holds correct {@link UserDetails}, false otherwise
     */
    public static boolean isUserAuthenticated() {
        return getUserDetails() != null;
    }

}
