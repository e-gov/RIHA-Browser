package ee.ria.riha.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Valentin Suhnjov
 */
public class RihaPreAuthenticatedUserDetailsService extends UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> {

    public RihaPreAuthenticatedUserDetailsService(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    /**
     * Retrieves user details from wrapped {@link UserDetailsService} and in case {@link UsernameNotFoundException}
     * produces {@link RihaUserDetails}. In case principal is of type {@link EstEIDPrincipal}, populates {@link
     * RihaUserDetails} with principal data.
     *
     * @param authentication pre-authenticated authentication tokem
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authentication) {
        UserDetails userDetails;
        try {
            userDetails = super.loadUserDetails(authentication);
        } catch (UsernameNotFoundException e) {
            userDetails = new RihaUserDetails(
                    new User(authentication.getName(), "", AuthorityUtils.NO_AUTHORITIES),
                    authentication.getName());
        }

        if (userDetails instanceof RihaUserDetails) {
            populateRihaUserDetails(((RihaUserDetails) userDetails), authentication);
        }

        return userDetails;
    }

    private void populateRihaUserDetails(RihaUserDetails userDetails, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof EstEIDPrincipal)) {
            return;
        }

        EstEIDPrincipal principal = ((EstEIDPrincipal) authentication.getPrincipal());
        userDetails.setFirstName(principal.getGivenName());
        userDetails.setLastName(principal.getSurname());
    }

}
