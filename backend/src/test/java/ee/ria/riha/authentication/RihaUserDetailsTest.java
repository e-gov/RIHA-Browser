package ee.ria.riha.authentication;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * @author Valentin Suhnjov
 */
public class RihaUserDetailsTest {

    private final String USERNAME = "john.doe";
    private final String PASSWORD = "strong";
    private final String PERSONAL_CODE = "12345";

    private final SimpleGrantedAuthority ROLE_TEST = new SimpleGrantedAuthority("ROLE_TEST");

    private final List<SimpleGrantedAuthority> WRAPPED_ROLES = Arrays.asList(ROLE_TEST);

    private final String TEST_ORG_CODE = "70000001";
    private final RihaOrganization TEST_ORG = new RihaOrganization(TEST_ORG_CODE, "Test AS");
    private final GrantedAuthority TEST_ORG_ROLE = new SimpleGrantedAuthority("ROLE_TEST_AS_USER");

    private final User WRAPPED_USER_DETAILS = new User(USERNAME, PASSWORD, WRAPPED_ROLES);

    private RihaUserDetails createRihaUserDetails() {
        return new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE,
                                   ImmutableMultimap.of(TEST_ORG, TEST_ORG_ROLE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenDelegateIsNull() {
        new RihaUserDetails(null, "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenPersonalCodeIsNull() {
        new RihaUserDetails(WRAPPED_USER_DETAILS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenPersonalCodeIsEmpty() {
        new RihaUserDetails(WRAPPED_USER_DETAILS, "");
    }

    @Test
    public void propagatesWrappedUserDetails() {
        RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);
        assertThat(rihaUserDetails.getUsername(), is(equalTo("john.doe")));
        assertThat(rihaUserDetails.getPassword(), is(equalTo("strong")));
        assertThat(rihaUserDetails.isEnabled(), is(true));
        assertThat(rihaUserDetails.isAccountNonExpired(), is(true));
        assertThat(rihaUserDetails.isAccountNonLocked(), is(true));
        assertThat(rihaUserDetails.isCredentialsNonExpired(), is(true));
    }

    @Test
    public void doesNotCombineAuthoritiesWhenNoActiveOrganizationSet() {
        RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);

        assertThat(rihaUserDetails.getAuthorities(), contains(ROLE_TEST));
    }

    @Test
    public void combinesWrappedAndOrganizationalAuthoritiesWhenOrganizationActive() {
        RihaUserDetails rihaUserDetails = createRihaUserDetails();
        rihaUserDetails.setActiveOrganization(TEST_ORG_CODE);

        assertThat(rihaUserDetails.getAuthorities(), containsInAnyOrder(ROLE_TEST, TEST_ORG_ROLE));
    }

    @Test
    public void doesNotDuplicateAuthoritiesWhenCombined() {
        SimpleGrantedAuthority organizationRole = new SimpleGrantedAuthority(ROLE_TEST.getAuthority());
        RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE,
                                                              ImmutableMultimap.of(TEST_ORG, organizationRole));
        rihaUserDetails.setActiveOrganization(TEST_ORG_CODE);

        assertThat(rihaUserDetails.getAuthorities(), contains(ROLE_TEST));
    }

    @Test
    public void setsActiveOrganizationWhenValidCodeProvided() {
        RihaUserDetails rihaUserDetails = createRihaUserDetails();

        rihaUserDetails.setActiveOrganization(TEST_ORG_CODE);

        assertThat(rihaUserDetails.getActiveOrganization(), is(equalTo(TEST_ORG)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenSettingNonExistentActiveOrganizationCode() {
        RihaUserDetails rihaUserDetails = createRihaUserDetails();

        rihaUserDetails.setActiveOrganization("nonexistent");

        assertThat(rihaUserDetails.getActiveOrganization(), is(equalTo(TEST_ORG)));
    }

    @Test
    public void resetsActiveOrganizationWhenCodeIsNull() {
        RihaUserDetails rihaUserDetails = createRihaUserDetails();

        rihaUserDetails.setActiveOrganization(TEST_ORG_CODE);
        rihaUserDetails.setActiveOrganization(null);

        assertThat(rihaUserDetails.getActiveOrganization(), is(nullValue()));
    }

}