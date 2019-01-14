package ee.ria.riha.conf;

import ee.ria.riha.authentication.CustomisedNimbusJwtDecoderJwkSupport;
import ee.ria.riha.authentication.RihaFilterBasedLdapUserSearch;
import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties.LdapAuthenticationProperties;
import ee.ria.riha.conf.ApplicationProperties.LdapProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapUserDetailsService ldapUserDetailsService;

    @Autowired
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public LdapUserDetailsService ldapUserDetailsService(ApplicationProperties applicationProperties,
                                                         LdapContextSource contextSource) {
        LdapAuthenticationProperties ldapAuthenticationProperties = applicationProperties.getLdapAuthentication();
        RihaFilterBasedLdapUserSearch userSearch = new RihaFilterBasedLdapUserSearch(
                ldapAuthenticationProperties.getUserSearchBase(),
                ldapAuthenticationProperties.getUserSearchFilter(),
                contextSource);

        LdapUserDetailsService userDetailsService = new LdapUserDetailsService(userSearch);
        userDetailsService.setUserDetailsMapper(new RihaLdapUserDetailsContextMapper(contextSource));

        return userDetailsService;
    }

    @Bean
    public LdapContextSource contextSource(ApplicationProperties applicationProperties) {
        LdapContextSource contextSource = new LdapContextSource();

        LdapProperties ldapProperties = applicationProperties.getLdap();
        contextSource.setUrl(ldapProperties.getUrl());
        contextSource.setBase(ldapProperties.getBaseDn());
        contextSource.setUserDn(ldapProperties.getUser());
        contextSource.setPassword(ldapProperties.getPassword());

        return contextSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http
                .csrf().disable() // needed for JWT verification
                .cors().disable()

                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                        .and()
                .oauth2Login()
                .loginPage(applicationProperties.getBaseUrl())
                .successHandler((request, response, authentication) -> {
                    log.info("Kasutaja {} ID koodiga {} logis sisse kasutades amr: {} ",
                            ((RihaUserDetails) authentication.getPrincipal()).getFullName(),
                            ((RihaUserDetails) authentication.getPrincipal()).getPersonalCode(),
                            ((RihaUserDetails) authentication.getPrincipal()).getTaraAmr()
                    );
                    response.sendRedirect("/Login");
                })
                .redirectionEndpoint()
                .baseUri("/authenticate")
                .and()
                .userInfoEndpoint()
                .customUserType(RihaUserDetails.class, applicationProperties.getTara().getRegistrationId())
                .oidcUserService(userRequest -> {
                    RihaUserDetails rihaUserDetails;
                    String personalCode = userRequest.getIdToken().getSubject();
                    try {
                        UserDetails userDetails = ldapUserDetailsService.loadUserByUsername(personalCode);
                        rihaUserDetails = (RihaUserDetails) userDetails;
                    } catch (UsernameNotFoundException e) {
                        //this means that the LDAP does not contain record with such personal code
                        rihaUserDetails = getDefaultRihaUserWithDefaultRole(personalCode);
                    } catch (Exception e) {
                        log.error("auth error", e);
                        throw new OAuth2AuthenticationException(new OAuth2Error("401"), e);
                    }
                    rihaUserDetails.setUserRequest(userRequest);
                    if (userRequest.getIdToken().getClaims().get("profile_attributes") instanceof Map) {
                        Map<String, String> profileAttributes = (Map<String, String>) userRequest.getIdToken().getClaims().get("profile_attributes");
                        rihaUserDetails.setFirstName(profileAttributes.get("given_name"));
                        rihaUserDetails.setLastName(profileAttributes.get("family_name"));
                    }

                    rihaUserDetails.setUserRequest(userRequest);
                    return rihaUserDetails;
                })
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient)
                .and()
                .addObjectPostProcessor(new CustomPostProcessor());
    }

    private RihaUserDetails getDefaultRihaUserWithDefaultRole(String personalCode) {
        RihaUserDetails rihaUserDetails;
        LdapUserDetailsImpl.Essence userDetailsEssence = new LdapUserDetailsImpl.Essence(new DirContextAdapter());
        userDetailsEssence.setUsername(personalCode);
        userDetailsEssence.addAuthority(new SimpleGrantedAuthority(RihaLdapUserDetailsContextMapper.DEFAULT_RIHA_USER_ROLE));

        rihaUserDetails = new RihaUserDetails(userDetailsEssence.createUserDetails(), personalCode);
        return rihaUserDetails;
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(ApplicationProperties applicationProperties) {
        ApplicationProperties.TaraProperties taraConfig = applicationProperties.getTara();
        return new InMemoryClientRegistrationRepository(
                ClientRegistration
                        .withRegistrationId(taraConfig.getRegistrationId())
                        .authorizationUri(taraConfig.getUserAuthorizationUri())
                        .clientId(taraConfig.getClientId())
                        .clientName(taraConfig.getClientId())
                        .clientSecret(taraConfig.getClientSecret())
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUriTemplate(taraConfig.getRegisteredRedirectUri())
                        .tokenUri(taraConfig.getAccessTokenUri())
                        .jwkSetUri(taraConfig.getJwkKeySetUri())
                        .scope(taraConfig.getScope())
                        .build()
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(ApplicationProperties applicationProperties) {
        return new CustomisedNimbusJwtDecoderJwkSupport(applicationProperties.getTara().getJwkKeySetUri());
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    /**
     * This is a workaround for TARA not fully complying with OAuth2 spec.
     * It's needed to inject a custom jwtDecoder into the AuthorizationCodeAuthenticationProvider
     * <p>
     * It seems that there is no standard clean mechanism to inject a custom jwtDecoder using spring.
     */
    private class CustomPostProcessor implements org.springframework.security.config.annotation.ObjectPostProcessor<OidcAuthorizationCodeAuthenticationProvider> {

        @Override
        public OidcAuthorizationCodeAuthenticationProvider postProcess(OidcAuthorizationCodeAuthenticationProvider authenticationProvider) {

            try {
                Field jwtDecoders = authenticationProvider.getClass().getDeclaredField("jwtDecoders");
                jwtDecoders.setAccessible(true);
                ((Map<String, JwtDecoder>) jwtDecoders.get(authenticationProvider)).put(applicationProperties.getTara().getRegistrationId(), jwtDecoder);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return authenticationProvider;
        }

    }

}