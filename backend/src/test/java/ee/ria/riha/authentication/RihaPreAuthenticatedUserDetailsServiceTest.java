package ee.ria.riha.authentication;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class RihaPreAuthenticatedUserDetailsServiceTest {

    private final String SERIAL_NUMBER = "12345678901";
    private final String PRINCIPAL_SN = "DOE";
    private final String PRINCIPAL_GN = "JOHN";

    private final RihaUserDetails EXISTING_USER = new RihaUserDetails(
            new User(SERIAL_NUMBER, "", AuthorityUtils.NO_AUTHORITIES), SERIAL_NUMBER);

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private RihaPreAuthenticatedUserDetailsService rihaPreAuthenticatedUserDetailsService;

    private PreAuthenticatedAuthenticationToken estEidAuthenticationToken;

    @Before
    public void setUp() {
        EstEIDPrincipal principal = new EstEIDPrincipal(SERIAL_NUMBER);
        principal.setSurname(PRINCIPAL_SN);
        principal.setGivenName(PRINCIPAL_GN);

        estEidAuthenticationToken = new PreAuthenticatedAuthenticationToken(principal, null);

        when(userDetailsService.loadUserByUsername(principal.getName())).thenReturn(EXISTING_USER);
    }

    @Test
    public void fillsUserNameDetailsWhenPrincipalIsEstEIDPrincipal() {
        RihaUserDetails rihaUserDetails = (RihaUserDetails) rihaPreAuthenticatedUserDetailsService.loadUserDetails(
                estEidAuthenticationToken);

        assertThat(rihaUserDetails.getFirstName(), is(equalTo(PRINCIPAL_GN)));
        assertThat(rihaUserDetails.getLastName(), is(equalTo(PRINCIPAL_SN)));
    }

    @Test
    public void createsUserDetailsEvenIfUserNotFound() {
        when(userDetailsService.loadUserByUsername(any())).thenThrow(new UsernameNotFoundException("mock"));

        UserDetails userDetails = rihaPreAuthenticatedUserDetailsService.loadUserDetails(estEidAuthenticationToken);

        assertThat(userDetails, is(notNullValue()));
    }

}
