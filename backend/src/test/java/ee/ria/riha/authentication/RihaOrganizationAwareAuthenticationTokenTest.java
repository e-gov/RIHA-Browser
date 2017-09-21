package ee.ria.riha.authentication;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Valentin Suhnjov
 */
public class RihaOrganizationAwareAuthenticationTokenTest {

    private static final User MARY_ANN_USER = new User("11412090004", "", AuthorityUtils.NO_AUTHORITIES);

    private static final SimpleGrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("ROLE_RIHA_USER");
    private static final SimpleGrantedAuthority CUSTOM_ROLE = new SimpleGrantedAuthority("ROLE_CUSTOM");

    private static final String TEST_ORG_CODE = "70000001";
    private static final RihaOrganization TEST_ORG = new RihaOrganization(TEST_ORG_CODE, "Test ORG");
    private static final GrantedAuthority TEST_ORG_ROLE = new SimpleGrantedAuthority("ROLE_TEST_ORG_USER");

    private RihaUserDetails principal = new RihaUserDetails(MARY_ANN_USER, MARY_ANN_USER.getUsername(),
                                                            ImmutableMultimap.of(TEST_ORG, TEST_ORG_ROLE));
    private RihaUserDetails principalNoAuthorities = new RihaUserDetails(MARY_ANN_USER, MARY_ANN_USER.getUsername(),
            ImmutableMultimap.of());
    private Object credentials = null;
    private Collection<? extends GrantedAuthority> authorities = Collections.singletonList(DEFAULT_ROLE);
    private Collection<? extends GrantedAuthority> missingAuthorities = Collections.emptyList();

    @Test
    public void combinesBaseAuthoritiesAndActiveOrganizationAuthorities() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        authenticationToken.setActiveOrganization(TEST_ORG_CODE);

        assertThat(authenticationToken.getAuthorities(), containsInAnyOrder(DEFAULT_ROLE, TEST_ORG_ROLE));
    }

    private RihaOrganizationAwareAuthenticationToken createAuthenticationToken() {
        return new RihaOrganizationAwareAuthenticationToken(principal, credentials, authorities);
    }

    @Test
    public void doesNotDuplicateAuthoritiesWhenCombined() {
        // Assign single organization to Mary Ann with role ROLE_TEST
        principal = new RihaUserDetails(MARY_ANN_USER, MARY_ANN_USER.getUsername(),
                                        ImmutableMultimap.of(TEST_ORG, CUSTOM_ROLE));

        // Base role is also ROLE_TEST
        authorities = Collections.singletonList(CUSTOM_ROLE);

        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        authenticationToken.setActiveOrganization(TEST_ORG_CODE);

        // when base role and organization role combined, only single CUSTOM_ROLE should exists
        assertThat(authenticationToken.getAuthorities(), contains(CUSTOM_ROLE));
    }

    @Test
    public void setsActiveOrganizationWhenValidCodeProvided() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        authenticationToken.setActiveOrganization(TEST_ORG_CODE);

        assertThat(authenticationToken.getActiveOrganization(), is(equalTo(TEST_ORG)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenSettingNonExistentActiveOrganizationCode() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        authenticationToken.setActiveOrganization("nonexistent");
    }

    @Test
    public void resetsActiveOrganizationWhenCodeIsNull() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        authenticationToken.setActiveOrganization(TEST_ORG_CODE);
        authenticationToken.setActiveOrganization(null);

        assertThat(authenticationToken.getActiveOrganization(), is(nullValue()));
    }

    @Test
    public void doesNotCombineAuthoritiesWhenNoActiveOrganizationSet() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = createAuthenticationToken();

        assertThat(authenticationToken.getAuthorities(), contains(DEFAULT_ROLE));
    }

    @Test
    public void authoritiesIsNotNullWhenUserHasNoAuthorities() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = new RihaOrganizationAwareAuthenticationToken(
                principalNoAuthorities, credentials, missingAuthorities);

        assertThat(authenticationToken.getAuthorities(), is(notNullValue()));
    }

    @Test
    public void authoritiesIsEmptyWhenUserHasNoAuthorities() {
        RihaOrganizationAwareAuthenticationToken authenticationToken = new RihaOrganizationAwareAuthenticationToken(
                principalNoAuthorities, credentials, missingAuthorities);

        assertThat(authenticationToken.getAuthorities(), is(emptyCollectionOf(GrantedAuthority.class)));
    }
}