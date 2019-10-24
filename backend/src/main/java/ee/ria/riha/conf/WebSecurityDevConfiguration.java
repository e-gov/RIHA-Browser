package ee.ria.riha.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("dev")
public class WebSecurityDevConfiguration extends WebSecurityConfiguration {
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
		http
			.csrf().disable()
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
}
