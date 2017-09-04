package ee.ria.riha.conf;

import ee.ria.riha.authentication.EstEIDRequestHeaderAuthenticationFilter;
import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.conf.ApplicationProperties.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * @author Valentin Suhnjov
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ALL_NON_OPERATIONAL_ATTRIBUTES = "*";
    private static final String MEMBER_OF_ATTRIBUTE = "memberOf";

    @Autowired
    private LdapUserDetailsService ldapUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getEsteidPreAuthenticatedAuthenticationProvider());
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService(ApplicationProperties applicationProperties,
                                                         LdapContextSource contextSource) {
        AuthenticationProperties authentication = applicationProperties.getAuthentication();
        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(
                authentication.getUserSearchBase(),
                authentication.getUserSearchFilter(),
                contextSource);
        userSearch.setReturningAttributes(new String[]{ALL_NON_OPERATIONAL_ATTRIBUTES, MEMBER_OF_ATTRIBUTE});
        userSearch.setSearchSubtree(true);

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
                new UserDetailsByNameServiceWrapper<>(ldapUserDetailsService));

        return authenticationProvider;
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