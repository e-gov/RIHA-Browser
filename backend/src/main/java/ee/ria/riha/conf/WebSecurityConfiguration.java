package ee.ria.riha.conf;

import ee.ria.riha.authentication.EstEIDRequestHeaderAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Valentin Suhnjov
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getEsteidPreAuthenticatedAuthenticationProvider());

        auth.inMemoryAuthentication();
    }

    private PreAuthenticatedAuthenticationProvider getEsteidPreAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(getPreAuthenticatedUserDetailsService());

        return authenticationProvider;
    }

    private UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> getPreAuthenticatedUserDetailsService() {
        return new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(esteidRequestHeaderAuthenticationFilter(authenticationManager()));

        http.authorizeRequests()
                .antMatchers("/idlogin").authenticated()
                .anyRequest().permitAll();
    }

    private EstEIDRequestHeaderAuthenticationFilter esteidRequestHeaderAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        EstEIDRequestHeaderAuthenticationFilter filter = new EstEIDRequestHeaderAuthenticationFilter();
        filter.setExceptionIfHeaderMissing(false);
        filter.setPrincipalRequestHeader("SSL_CLIENT_S_DN");
        filter.setCredentialsRequestHeader("SSL_CLIENT_CERT");
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

}