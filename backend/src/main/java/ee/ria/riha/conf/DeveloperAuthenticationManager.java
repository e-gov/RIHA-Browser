package ee.ria.riha.conf;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl.Essence;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties.DeveloperUser;
import ee.ria.riha.conf.ApplicationProperties.Organization;

@Component
public class DeveloperAuthenticationManager implements AuthenticationManager {

	@Autowired
	ApplicationProperties applicationProperties;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		DeveloperUser developerUser = applicationProperties.getDeveloperUser();
		String name = developerUser.getName();
		String code = developerUser.getCode();

		Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setUsername(code);
		essence.setDn(name);
		essence.addAuthority(new SimpleGrantedAuthority(RihaLdapUserDetailsContextMapper.DEFAULT_RIHA_USER_ROLE));
		LdapUserDetails ldapUserDetails = essence.createUserDetails();

		Multimap<RihaOrganization, GrantedAuthority> organizationRoles = developerUser.getOrganizations().stream()
				.collect(
						ArrayListMultimap::create,
						(map, org) -> map.putAll(
								new RihaOrganization(org.getCode(), org.getName()),
								Arrays.stream(org.getRoles())
										.map(SimpleGrantedAuthority::new)
										.collect(Collectors.toSet())),
						(map, u) -> {});

		RihaUserDetails userDetails = new RihaUserDetails(ldapUserDetails, code, organizationRoles);
		userDetails.setFirstName(name);
		userDetails.setLastName(name);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				userDetails,
				authentication.getCredentials(),
				developerUser.getOrganizations().stream()
						.map(Organization::getRoles)
						.flatMap(Arrays::stream)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet())
		);

		SecurityContextHolder.getContext().setAuthentication(token);
		return token;
	}
}
