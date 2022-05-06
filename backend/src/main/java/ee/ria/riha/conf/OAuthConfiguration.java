package ee.ria.riha.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuthConfiguration {

    @Bean
    @Profile("!dev")
    public ClientRegistrationRepository clientRegistrationRepository(ApplicationProperties applicationProperties) {
        ApplicationProperties.TaraProperties taraConfig = applicationProperties.getTara();
        return new InMemoryClientRegistrationRepository(
                ClientRegistration
                        .withRegistrationId(taraConfig.getRegistrationId())
                        .authorizationUri(taraConfig.getUserAuthorizationUri())
                        .clientId(taraConfig.getClientId())
                        .clientName(taraConfig.getClientId())
                        .clientSecret(taraConfig.getClientSecret())
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri(taraConfig.getRegisteredRedirectUri())
                        .tokenUri(taraConfig.getAccessTokenUri())
                        .jwkSetUri(taraConfig.getJwkKeySetUri())
                        .scope(taraConfig.getScope())
                        .build()
        );
    }

}
