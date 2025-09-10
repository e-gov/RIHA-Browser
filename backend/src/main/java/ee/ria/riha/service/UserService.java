package ee.ria.riha.service;

import ee.ria.riha.authentication.*;
import ee.ria.riha.domain.*;
import ee.ria.riha.domain.model.*;
import ee.ria.riha.web.model.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.*;
import javax.naming.ldap.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Valentin Suhnjov
 */
@Slf4j
@Service
public class UserService {

  private LdapRepository ldapRepository;

  /**
   * Changes active organization of currently logged in user.
   *
   * @param organizationCode - organization code (registry number)
   */
  public void changeActiveOrganization(String organizationCode) {
    log.info(
        "UserService.changeActiveOrganization called with organizationCode: {}", organizationCode);

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    log.info("Authentication in UserService: {}", authentication);

    Assert.notNull(authentication, "authentication not found in security context");

    if (!(authentication.getPrincipal() instanceof RihaUserDetails)) {
      log.error(
          "Principal is not RihaUserDetails, but: {}", authentication.getPrincipal().getClass());
      throw new IllegalStateException(
          "Organization change is not supported by current authentication");
    }

    RihaUserDetails rihaUserDetails = (RihaUserDetails) authentication.getPrincipal();
    log.info(
        "RihaUserDetails found, organizations available: {}",
        rihaUserDetails.getOrganizationsByCode() != null
            ? rihaUserDetails.getOrganizationsByCode().keySet()
            : "null");

    if (rihaUserDetails.getOrganizationsByCode() != null) {
      rihaUserDetails.setActiveOrganization(
          rihaUserDetails.getOrganizationsByCode().get(organizationCode));
      log.info("Set active organization to: {}", rihaUserDetails.getActiveOrganization());

      // In Spring Security 6.x, we need to explicitly save the context to the session
      SecurityContext newContext = SecurityContextHolder.createEmptyContext();
      newContext.setAuthentication(authentication);
      SecurityContextHolder.setContext(newContext);

      // Force save the security context to the session
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      HttpSession session = request.getSession();
      session.setAttribute("SPRING_SECURITY_CONTEXT", newContext);
    } else {
      log.warn("No organizations available for user");
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
