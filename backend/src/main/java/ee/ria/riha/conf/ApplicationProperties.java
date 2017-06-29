package ee.ria.riha.conf;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@ConfigurationProperties(prefix = "browser")
public class ApplicationProperties {

    private final StorageClient storageClient = new StorageClient();

    public StorageClient getStorageClient() {
        return storageClient;
    }

    public static class StorageClient {
        @NotEmpty
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
