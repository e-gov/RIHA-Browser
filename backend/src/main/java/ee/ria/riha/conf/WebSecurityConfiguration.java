package ee.ria.riha.conf;

import ee.ria.riha.authentication.EstEIDRequestHeaderAuthenticationFilter;
import ee.ria.riha.authentication.RihaFilterBasedLdapUserSearch;
import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.authentication.RihaPreAuthenticatedUserDetailsService;
import ee.ria.riha.conf.ApplicationProperties.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * @author Valentin Suhnjov
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapUserDetailsService ldapUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getEsteidPreAuthenticatedAuthenticationProvider());
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService(ApplicationProperties applicationProperties,
                                                         LdapContextSource contextSource) {
        AuthenticationProperties authenticationProperties = applicationProperties.getAuthentication();
        RihaFilterBasedLdapUserSearch userSearch = new RihaFilterBasedLdapUserSearch(
                authenticationProperties.getUserSearchBase(),
                authenticationProperties.getUserSearchFilter(),
                contextSource);

        LdapUserDetailsService userDetailsService = new LdapUserDetailsService(userSearch);
        userDetailsService.setUserDetailsMapper(new RihaLdapUserDetailsContextMapper(contextSource));

        return userDetailsService;
    }

    @Bean
    public LdapContextSource contextSource(ApplicationProperties applicationProperties) {
        LdapContextSource contextSource = new LdapContextSource();

        AuthenticationProperties authentication = applicationProperties.getAuthentication();
        contextSource.setUrl(authentication.getLdapUrl());
        contextSource.setBase(authentication.getLdapBaseDn());
        contextSource.setUserDn(authentication.getLdapUser());
        contextSource.setPassword(authentication.getLdapPassword());

        return contextSource;
    }

    private PreAuthenticatedAuthenticationProvider getEsteidPreAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new RihaPreAuthenticatedUserDetailsService(ldapUserDetailsService));

        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.addFilter(esteidRequestHeaderAuthenticationFilter(authenticationManager()));

        http.authorizeRequests()
                .anyRequest().permitAll();

        http.logout().logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
    }

    private EstEIDRequestHeaderAuthenticationFilter esteidRequestHeaderAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        EstEIDRequestHeaderAuthenticationFilter filter = new EstEIDRequestHeaderAuthenticationFilter(
                new AntPathRequestMatcher("/login/esteid", "GET"));
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

}