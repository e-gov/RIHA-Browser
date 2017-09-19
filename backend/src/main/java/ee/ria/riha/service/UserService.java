package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Valentin Suhnjov
 */
@Service
public class UserService {

    /**
     * Changes active organization of currently logged in user.
     *
     * @param organizationCode - organization code (registry number)
     */
    public void changeActiveOrganization(String organizationCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(authentication, "authentication not found in security context");

        if (!(authentication instanceof RihaOrganizationAwareAuthenticationToken)) {
            throw new IllegalStateException("Organization change is not supported by current authentication");
        }

        ((RihaOrganizationAwareAuthenticationToken) authentication).setActiveOrganization(organizationCode);
    }

}
