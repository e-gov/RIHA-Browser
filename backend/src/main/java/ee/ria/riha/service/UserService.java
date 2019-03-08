package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.LdapRepository;
import ee.ria.riha.domain.model.LdapUser;
import ee.ria.riha.web.model.UserDetailsModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import java.util.List;
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

        if (!(authentication.getPrincipal() instanceof RihaUserDetails)) {
            throw new IllegalStateException("Organization change is not supported by current authentication");
        }

        RihaUserDetails rihaUserDetails = (RihaUserDetails) authentication.getPrincipal();

        if (rihaUserDetails.getOrganizationsByCode() != null) {
            rihaUserDetails.setActiveOrganization(rihaUserDetails.getOrganizationsByCode().get(organizationCode));
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
        return getApproversEmails(ldapRepository.getApproversByOrganization(organizationCode));
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
