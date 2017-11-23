package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.domain.LdapRepository;
import ee.ria.riha.domain.model.LdapUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentin Suhnjov
 */
@Service
public class UserService {

    private LdapRepository ldapRepository;

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
     * @param personalCodes - set of unique personal codes
     * @return - set of unique emails, excluding null values
     */
    public Set<String> getEmailsByPersonalCodes(Set<String> personalCodes) {
        return ldapRepository.findLdapUsersByPersonalCodes(personalCodes).stream()
                .map(LdapUser::getMail)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Returns emails of all RIHA assessors (RIHA-hindajad).
     *
     * @return set of unique assessors emails, excluding null values
     */
    public Set<String> getAssessorsEmails() {
        return ldapRepository.getAllAssessors().stream()
                .map(LdapUser::getMail)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Autowired
    public void setLdapRepository(LdapRepository ldapRepository) {
        this.ldapRepository = ldapRepository;
    }
}
