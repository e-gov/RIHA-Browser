package ee.ria.riha.service;

import ee.ria.riha.authentication.*;
import ee.ria.riha.domain.*;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.web.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;
import java.util.stream.*;
import javax.mail.internet.*;
import javax.naming.ldap.*;

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
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Assert.notNull(authentication, "authentication not found in security context");

        if (!(authentication.getPrincipal() instanceof RihaUserDetails)) {
            throw new IllegalStateException("Organization change is not supported by current authentication");
        }

        RihaUserDetails rihaUserDetails = (RihaUserDetails) authentication.getPrincipal();

        if (rihaUserDetails.getOrganizationsByCode() != null) {
            rihaUserDetails.setActiveOrganization(rihaUserDetails.getOrganizationsByCode().get(organizationCode));
            SecurityContext newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
        }
    }

    /**
     * Resolves set of emails by set of personal codes.
     *
     * @param personalCodes - set of unique personal codes
     * @return - set of unique emails, excluding null values
     */
    public Set<String> getEmailsByPersonalCodes(Set<String> personalCodes) {
        return getApproversEmails(ldapRepository.findLdapUsersByPersonalCodes(personalCodes));
    }

    /**
     * Returns emails of all RIHA approvers (RIHA-hindajad).
     *
     * @return set of unique approvers emails, excluding null values
     */
    public Set<String> getApproversEmails() {
        return getApproversEmails(ldapRepository.getAllApprovers());
    }

    /**
     * Returns emails of all approvers of given organization.
     *
     * @return set of unique approvers emails, excluding null values
     */
    public Set<String> getApproversEmailsByOrganization(String organizationCode) {
        return getApproversEmails(
                organizationCode == null
                        ? ldapRepository.getAllApprovers()
                        : ldapRepository.getApproversByOrganization(organizationCode));
    }

    private Set<String> getApproversEmails(List<LdapUser> approvers) {
        return approvers.stream()
                .map(LdapUser::getMail)
                .filter(Objects::nonNull)
                .filter(this::isValidEmailAddress)
                .collect(Collectors.toSet());
    }

    public List<UserDetailsModel> getUsersByOrganization(String organizationCode) {
        return ldapRepository.getUsersByOrganization(organizationCode).stream()
                .map(user -> this.toUserModel(user, organizationCode))
                .collect(Collectors.toList());
    }

    private UserDetailsModel toUserModel(LdapUser ldapUser, String organizationCode) {
        return UserDetailsModel.builder()
                .firstName(ldapUser.getGivenName())
                .lastName(ldapUser.getSurname())
                .email(ldapUser.getMail())
                .approver(hasRole(ldapUser, organizationCode + "-hindaja"))
                .producer(hasRole(ldapUser, organizationCode + "-kirjeldaja"))
                .build();
    }

    private boolean hasRole(LdapUser ldapUser, String role) {
        return ldapUser.getMemberOf().stream()
                .map(LdapName.class::cast)
                .map(LdapName::getRdns)
                .flatMap(List::stream)
                .filter(rdn -> rdn.getType().equals("cn"))
                .map(Rdn::getValue)
                .anyMatch(role::equals);
    }

    @Autowired
    public void setLdapRepository(LdapRepository ldapRepository) {
        this.ldapRepository = ldapRepository;
    }

    private boolean isValidEmailAddress(String toValidate) {
        try {
            new InternetAddress(toValidate).validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }
}
