package ee.ria.riha.conf;

import ee.ria.riha.storage.client.StorageClient;
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
    public StorageClient storageClient(ApplicationProperties applicationProperties) {
        return new StorageClient(new RestTemplate(), applicationProperties.getStorageUrl());
    }

}
