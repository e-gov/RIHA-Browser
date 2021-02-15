package ee.ria.riha.conf;

import ee.ria.riha.authentication.*;
import ee.ria.riha.conf.ApplicationProperties.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.ldap.userdetails.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.oidc.authentication.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.access.channel.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.security.web.csrf.*;
import org.springframework.web.util.*;

import java.lang.reflect.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@Profile("!dev")
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // parameter passed from frontend. Contains the URL from where the login button was clicked.
    private static final String REDIRECT_URL_PARAMETER_MARKER = "fromUrl";

    static final String TARA_AUTH_ENDPOINT = "/oauth2/authorization/tara";

    @Autowired
    private LdapUserDetailsService ldapUserDetailsService;

    @Autowired
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    protected ApplicationProperties applicationProperties;

    @Value("${csp.policyDirective}")
    private String policyDirective;

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
        contextSource.setAuthenticationStrategy(new DefaultTlsDirContextAuthenticationStrategy());
        contextSource.setUrl(ldapProperties.getUrl());
        contextSource.setBase(ldapProperties.getBaseDn());
        contextSource.setUserDn(ldapProperties.getUser());
        contextSource.setPassword(ldapProperties.getPassword());

        return contextSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (StringUtils.isNotBlank(policyDirective)) {
            http.headers()
                    .contentSecurityPolicy(policyDirective);
        }

        http
                .csrf().csrfTokenRepository(csrfTokenRepository()).and()
                .cors().disable()

                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .and()
                .addFilterBefore(createFromUrlSessionFilter(), ChannelProcessingFilter.class)
                .oauth2Login()
                .loginPage(applicationProperties.getBaseUrl())
                .successHandler(successHandler())
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

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
        cookieCsrfTokenRepository.setCookieHttpOnly(false);
        cookieCsrfTokenRepository.setCookiePath("/");
        return cookieCsrfTokenRepository;
    }

    protected AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
			log.info("Kasutaja {} ID koodiga {} logis sisse kasutades amr: {} ",
					((RihaUserDetails) authentication.getPrincipal()).getFullName(),
					((RihaUserDetails) authentication.getPrincipal()).getPersonalCode(),
					((RihaUserDetails) authentication.getPrincipal()).getTaraAmr()
			);

			String fromUrl = (String) request.getSession(false).getAttribute(REDIRECT_URL_PARAMETER_MARKER);

			if (fromUrl != null) {
				// fromUrl param has the following format:
				// /url?param1=paramValue&param2=param2Value...
				// should transform question marks into param delimiters
				fromUrl = fromUrl.replaceAll("\\?", "&");

				response.sendRedirect("/Login?" + UriUtils.encodePath("fromUrl=" + fromUrl, "UTF-8"));
			} else {
				response.sendRedirect("/Login");
			}
		};
    }

    protected Filter createFromUrlSessionFilter() {
        return (request, response, chain) -> {
            if (request instanceof HttpServletRequest && ((HttpServletRequest) request).getRequestURI().contains(TARA_AUTH_ENDPOINT)) {
                String fromUrlParameter = request.getParameter(REDIRECT_URL_PARAMETER_MARKER);
                log.info("authenticate request detected, fromUrl param ({}) is saved to session ", fromUrlParameter);
                ((HttpServletRequest) request).getSession().setAttribute("fromUrl", fromUrlParameter);
            }
            chain.doFilter(request, response);
        };
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
    @Profile("!dev")
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