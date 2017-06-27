package ee.ria.riha.conf;

import ee.ria.riha.storage.client.StorageClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configures RIHA-Storage REST client.
 *
 * @author Valentin Suhnjov
 */
@Configuration
@ConfigurationProperties(prefix = "storageClient")
public class StorageClientConfiguration {

    private String baseUrl;

    @Bean
    public StorageClient storageClient() {
        return new StorageClient(new RestTemplate(), baseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
