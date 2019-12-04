package ee.ria.riha.conf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("dev")
public class WebSecurityDevConfiguration extends WebSecurityConfiguration {


	@Value("${csp.policyDirective}")
	private String policyDirective;

	@Bean
	protected UsernamePasswordAuthenticationFilter authenticationFilter(DeveloperAuthenticationManager authenticationManager) {
		UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager);
		authenticationFilter.setAuthenticationSuccessHandler(successHandler());
		authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(TARA_AUTH_ENDPOINT, HttpMethod.GET.name()));
		authenticationFilter.setPostOnly(Boolean.FALSE);
		return authenticationFilter;
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
			.addFilterBefore(createFromUrlSessionFilter(), ChannelProcessingFilter.class)
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
			.and()
			.formLogin()
				.permitAll()
				.loginPage("/oauth2/authorization/tara")
				.loginProcessingUrl("/oauth2/authorization/tara");
	}

	private CsrfTokenRepository csrfTokenRepository() {
		CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
		cookieCsrfTokenRepository.setCookieHttpOnly(false);
		cookieCsrfTokenRepository.setCookiePath("/");
		return cookieCsrfTokenRepository;
	}
}
