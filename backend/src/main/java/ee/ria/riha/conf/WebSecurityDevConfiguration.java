package ee.ria.riha.conf;

import ee.ria.riha.logging.auditlog.AuditLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@Profile("dev")
public class WebSecurityDevConfiguration extends WebSecurityConfiguration {

  @Value("${csp.policyDirective}")
  private String policyDirective;

  public WebSecurityDevConfiguration(AuditLogger auditLogger) {
    super(auditLogger);
  }

  @Bean
  protected UsernamePasswordAuthenticationFilter authenticationFilter(
      DeveloperAuthenticationManager authenticationManager) {
    UsernamePasswordAuthenticationFilter authenticationFilter =
        new UsernamePasswordAuthenticationFilter();
    authenticationFilter.setAuthenticationManager(authenticationManager);
    authenticationFilter.setAuthenticationSuccessHandler(successHandler());
    authenticationFilter.setRequiresAuthenticationRequestMatcher(
        request ->
            TARA_AUTH_ENDPOINT.equals(request.getRequestURI())
                && HttpMethod.GET.name().equals(request.getMethod()));
    authenticationFilter.setPostOnly(Boolean.FALSE);
    return authenticationFilter;
  }

  @Bean
  @Override
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    if (StringUtils.isNotBlank(policyDirective)) {
      http.headers(
          headers ->
              headers.contentSecurityPolicy(policy -> policy.policyDirectives(policyDirective)));
    }

    http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()))
        .cors(cors -> cors.disable())
        .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
        .exceptionHandling(
            handling -> handling.authenticationEntryPoint(authenticationEntryPoint()))
        .addFilterBefore(createFromUrlSessionFilter(), UsernamePasswordAuthenticationFilter.class)
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(
                        (new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))))
        .formLogin(
            login ->
                login
                    .permitAll()
                    .loginPage("/oauth2/authorization/tara")
                    .loginProcessingUrl("/oauth2/authorization/tara"));
    return http.build();
  }

  private CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
    cookieCsrfTokenRepository.setCookieCustomizer(
        cookie -> {
          cookie.httpOnly(false);
          cookie.path("/");
        });
    return cookieCsrfTokenRepository;
  }
}
