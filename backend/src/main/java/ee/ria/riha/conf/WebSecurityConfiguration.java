package ee.ria.riha.conf;

import ee.ria.riha.authentication.RihaFilterBasedLdapUserSearch;
import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties.LdapAuthenticationProperties;
import ee.ria.riha.conf.ApplicationProperties.LdapProperties;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import ee.ria.riha.logging.auditlog.AuditEvent;
import ee.ria.riha.logging.auditlog.AuditLogger;
import ee.ria.riha.logging.auditlog.AuditType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.util.UriUtils;

/**
 * @author Valentin Suhnjov
 */
@RequiredArgsConstructor
@Configuration
@Profile("!dev")
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // parameter passed from frontend. Contains the URL from where the login button was clicked.
    private static final String REDIRECT_URL_PARAMETER_MARKER = "fromUrl";

    static final String TARA_AUTH_ENDPOINT = "/oauth2/authorization/tara";

    @Autowired
    protected ApplicationProperties applicationProperties;

    public final AuditLogger auditLogger;

    @Value("${csp.policyDirective}")
    private String policyDirective;

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
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(createFromUrlSessionFilter(), ChannelProcessingFilter.class)
                .oauth2Login()
                .loginPage(applicationProperties.getBaseUrl())
                .successHandler(successHandler())
                .redirectionEndpoint()
                .baseUri("/authenticate")
                .and()
                .userInfoEndpoint()
                .oidcUserService(userRequest -> {
                    RihaUserDetails rihaUserDetails;
                    String personalCode = userRequest.getIdToken().getSubject();
                    try {
                        UserDetails userDetails = ldapUserDetailsService(applicationProperties,
                                contextSource(applicationProperties)).loadUserByUsername(personalCode);
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
                .accessTokenResponseClient(accessTokenResponseClient());
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

            auditLogger.log(AuditEvent.AUTH, AuditType.AUTHENTICATION, request, ((RihaUserDetails) authentication.getPrincipal()).getDelegate().getAuthorities().toString());

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

//    @Bean
//    @Profile("!dev")
//    ClientRegistrationRepository clientRegistrationRepository(ApplicationProperties applicationProperties) {
//        ApplicationProperties.TaraProperties taraConfig = applicationProperties.getTara();
//        return new InMemoryClientRegistrationRepository(
//                ClientRegistration
//                        .withRegistrationId(taraConfig.getRegistrationId())
//                        .authorizationUri(taraConfig.getUserAuthorizationUri())
//                        .clientId(taraConfig.getClientId())
//                        .clientName(taraConfig.getClientId())
//                        .clientSecret(taraConfig.getClientSecret())
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .redirectUri(taraConfig.getRegisteredRedirectUri())
//                        .tokenUri(taraConfig.getAccessTokenUri())
//                        .jwkSetUri(taraConfig.getJwkKeySetUri())
//                        .scope(taraConfig.getScope())
//                        .build()
//        );
//    }

    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }
}
