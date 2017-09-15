package ee.ria.riha.service;

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
