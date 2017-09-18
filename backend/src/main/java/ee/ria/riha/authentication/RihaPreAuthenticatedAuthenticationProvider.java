package ee.ria.riha.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * Wraps successful authentication with {@link RihaOrganizationAwareAuthenticationToken}.
 *
 * @author Valentin Suhnjov
 */
public class RihaPreAuthenticatedAuthenticationProvider extends PreAuthenticatedAuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) {
        Authentication authResult = super.authenticate(authentication);
        if (authResult == null) {
            return null;
        }

        return new RihaOrganizationAwareAuthenticationToken(
                authResult.getPrincipal(),
                authResult.getCredentials(),
                authResult.getAuthorities());
    }
}
