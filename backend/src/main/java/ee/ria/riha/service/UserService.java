package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.domain.UserDetailsLdapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * @author Valentin Suhnjov
 */
@Service
public class UserService {

    private UserDetailsLdapRepository userDetailsLdapRepository;

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

    /**
     * Returns users emails (if any exist) by users personal codes.
     *
     * @param ids - set of unique personal codes
     * @return - set of unique emails, excluding null values
     */
    public Set<String> getUsersEmailsByUsersIds(Set<String> ids) {
        return userDetailsLdapRepository.getEmailsByIds(ids);
    }

    @Autowired
    public void setUserDetailsLdapRepository(UserDetailsLdapRepository userDetailsLdapRepository) {
        this.userDetailsLdapRepository = userDetailsLdapRepository;
    }
}
