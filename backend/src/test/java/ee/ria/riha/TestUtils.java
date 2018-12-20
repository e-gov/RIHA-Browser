package ee.ria.riha;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import ee.ria.riha.authentication.RihaLdapUserDetailsContextMapper;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.service.JaneAuthenticationTokenBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.*;

import static ee.ria.riha.service.auth.RoleType.APPROVER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;

public class TestUtils {

    private static final String ACME_REG_CODE = "555010203";
    private static final String EVS_REG_CODE = "70001234";
    private static final String RIA_REG_CODE = "70006317";
    private static final UUID EXISTING_INFO_SYSTEM_UUID = UUID.fromString("01234567-0123-0123-0123-0123456789ab");


    public static Authentication getOAuth2LoginToken(ImmutableMultimap<RihaOrganization, GrantedAuthority> organizations, String organizationCode) {
        OAuth2AuthorizationRequest request = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("authorizationUri")
                .clientId("tara")
                .redirectUri("redirectUri"). build();
        OAuth2AuthorizationResponse response = OAuth2AuthorizationResponse
                .success("ok")
                .redirectUri("redirectUri")
                .build();
        OAuth2AuthorizationExchange authorizationRequest = new OAuth2AuthorizationExchange(request, response);


        Map<RihaOrganization, SimpleGrantedAuthority> organizationsMap = new HashMap<>();

        Multimap<RihaOrganization, GrantedAuthority> organizationMap = organizations != null
                ? organizations
                : ImmutableMultimap.of(
                new RihaOrganization(ACME_REG_CODE, "Acme org"),
                new SimpleGrantedAuthority(PRODUCER.getRole()),
                new RihaOrganization(EVS_REG_CODE, "Eesti Väikeloomaarstide Selts"),
                new SimpleGrantedAuthority(APPROVER.getRole()),
                new RihaOrganization(RIA_REG_CODE, "RIA"),
                new SimpleGrantedAuthority(APPROVER.getRole()));

        UserDetails userDetails = new LdapUserDetails() {
            @Override
            public String getDn() {
                return null;
            }

            @Override
            public void eraseCredentials() {

            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singleton(new SimpleGrantedAuthority(RihaLdapUserDetailsContextMapper.DEFAULT_RIHA_USER_ROLE));
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return null;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };
        OAuth2User rihaUserDetails = new RihaUserDetails(userDetails, JaneAuthenticationTokenBuilder.PERSONAL_CODE, organizationMap);
        ((RihaUserDetails) rihaUserDetails).setFirstName(JaneAuthenticationTokenBuilder.FIRST_NAME);
        ((RihaUserDetails) rihaUserDetails).setLastName(JaneAuthenticationTokenBuilder.LAST_NAME);


        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
        OAuth2AccessToken accessToken =  new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "tokenValue", Instant.now(), Instant.MAX);
        OAuth2LoginAuthenticationToken oAuth2LoginAuthenticationToken = new OAuth2LoginAuthenticationToken(
                ClientRegistration.withRegistrationId("tara")
                        .clientId("tara")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUriTemplate("redirectUriTemplate")
                        .authorizationUri("authorizationUri")
                        .tokenUri("tokenUri")

                        .build(),
                authorizationRequest,
                rihaUserDetails,
                authorities,
                accessToken);


        if (organizationCode != null) {
            setActiveOrganisation(oAuth2LoginAuthenticationToken, organizationCode);
        }

        return oAuth2LoginAuthenticationToken;
    }

    public static void setActiveOrganisation(Authentication authenticationToken, String organizationCode) {
        ((RihaUserDetails) authenticationToken.getPrincipal()).setActiveOrganization(organizationCode);
    }

}
