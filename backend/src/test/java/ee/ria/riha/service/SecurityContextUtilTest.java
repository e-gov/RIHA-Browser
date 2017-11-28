package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.rules.CleanAuthentication;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Valentin Suhnjov
 */
public class SecurityContextUtilTest {

    private RihaOrganizationAwareAuthenticationToken rihaAuthenticationToken = JaneAuthenticationTokenBuilder
            .builder()
            .build();

    private AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken(
            "anonymous", "anonymous", AuthorityUtils.createAuthorityList("ANONYMOUS"));

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

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
        rihaAuthenticationToken.setActiveOrganization("555010203");
        SecurityContextHolder.getContext().setAuthentication(rihaAuthenticationToken);

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
}