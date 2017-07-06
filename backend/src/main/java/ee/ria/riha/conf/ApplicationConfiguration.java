package ee.ria.riha.conf;

import ee.ria.riha.storage.client.StorageClient;
import ee.ria.riha.storage.domain.MainResourceRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    @Bean
    public MainResourceRepository mainResourceRepository(ApplicationProperties applicationProperties) {
        return new MainResourceRepository(getStorageClient(applicationProperties));
    }

    private StorageClient getStorageClient(ApplicationProperties applicationProperties) {
        RestTemplate restTemplate = new RestTemplate();
        return new StorageClient(restTemplate, applicationProperties.getStorageClient().getBaseUrl());
    }

}
