package ee.ria.riha.service;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.TestUtils;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.rules.CleanAuthentication;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static ee.ria.riha.service.auth.RoleType.APPROVER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Valentin Suhnjov
 */
public class SecurityContextUtilTest {

    private static final String ACME_REG_CODE = "555010203";
    private static final String RIA_REG_CODE = "70006317";
    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();
    ImmutableMultimap<RihaOrganization, GrantedAuthority> organizations = ImmutableMultimap.of(
                            new RihaOrganization(ACME_REG_CODE, "Acme org"),
                            new SimpleGrantedAuthority(PRODUCER.getRole()),
                            new RihaOrganization(RIA_REG_CODE, "RIA"),
                            new SimpleGrantedAuthority(APPROVER.getRole()));

    private Authentication rihaAuthenticationToken = TestUtils.getOAuth2LoginToken(organizations, null);

    private AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken(
            "anonymous", "anonymous", AuthorityUtils.createAuthorityList("ANONYMOUS"));

    @Test
    public void getUserDetailsReturnsEmptyOptionalWhenContextAuthenticationIsNotPresent() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertFalse(SecurityContextUtil.getUserDetails().isPresent());
    }

    @Test
    public void getUserDetailsReturnsEmptyWhenContextAuthenticationPrincipalIsNotOfUserDetailsType() {
        AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("anonymous",
                "anonymous", AuthorityUtils.createAuthorityList("ANONYMOUS"));

        SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);

        assertFalse(SecurityContextUtil.getUserDetails().isPresent());
    }

    @Test
    public void getUserDetailsReturnsUserDetails() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertTrue(SecurityContextUtil.getUserDetails().isPresent());
    }

    @Test
    public void getRihaUserDetailsReturnsEmptyOptionalWhenContextAuthenticationIsNotPresent() {
        assertFalse(SecurityContextUtil.getRihaUserDetails().isPresent());
    }

    @Test
    public void getRihaUserDetailsReturnsEmptyWhenContextAuthenticationPrincipalIsNotOfRihaUserDetailsType() {
        SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);

        assertFalse(SecurityContextUtil.getRihaUserDetails().isPresent());
    }

    @Test
    public void getRihaUserDetailsReturnsRihaUserDetails() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertTrue(SecurityContextUtil.getRihaUserDetails().isPresent());
    }

    @Test
    public void getActiveOrganizationReturnsEmptyWhenAuthenticationIsNotOfRihaType() {
        SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);

        assertFalse(SecurityContextUtil.getActiveOrganization().isPresent());
    }

    @Test
    public void getActiveOrganizationReturnsEmptyWhenActiveOrganizationWasNotSet() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertFalse(SecurityContextUtil.getActiveOrganization().isPresent());
    }

    @Test
    public void getActiveOrganizationReturnsOrganizationSet() {

        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);
        TestUtils.setActiveOrganisation(rihaAuthenticationToken, ACME_REG_CODE);

        assertTrue(SecurityContextUtil.getActiveOrganization().isPresent());
    }

    @Test
    public void hasRoleDoesNotFailWhenAuthenticationIsNotPresent() {
        SecurityContextUtil.hasRole("ROLE_RIHA_USER");
    }

    @Test
    public void hasRoleReturnsTrueWhenUserHasSpecifiedRole() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertTrue(SecurityContextUtil.hasRole("ROLE_RIHA_USER"));
    }

    @Test
    public void hasRoleReturnsFalseWhenUserDoesNotHaveSpecifiedRole() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertFalse(SecurityContextUtil.hasRole("ROLE_UNKNOWN"));
    }

    @Test
    public void hasRoleReturnsFalseWhenSpecifiedRoleIsNull() {
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertFalse(SecurityContextUtil.hasRole((String) null));
    }

    @Test
    public void isRiaApproverReturnsTrueWhenUserActiveOrganizationIsRiaAndUserHasApproverRole() {

        TestUtils.setActiveOrganisation(rihaAuthenticationToken, RIA_REG_CODE);
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertTrue(SecurityContextUtil.isRiaApprover());
    }

    @Test
    public void isRiaApproverReturnsFalseWhenUserActiveOrganizationIsRiaButUserDoesNotHaveApproverRole() {


        ImmutableMultimap<RihaOrganization, GrantedAuthority> ria = ImmutableMultimap.of(
                new RihaOrganization(RIA_REG_CODE, "RIA"),
                new SimpleGrantedAuthority(PRODUCER.getRole()));

        rihaAuthenticationToken = TestUtils.getOAuth2LoginToken(ria, RIA_REG_CODE);

        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertFalse(SecurityContextUtil.isRiaApprover());
    }

    @Test
    public void isRiaApproverReturnsFalseWhenUserActiveOrganizationIsNotRiaButUserHasApproverRole() {

        TestUtils.setActiveOrganisation(rihaAuthenticationToken, ACME_REG_CODE);
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

        assertFalse(SecurityContextUtil.isRiaApprover());
    }
}