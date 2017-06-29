package ee.ria.riha.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@ConfigurationProperties(prefix = "browser")
public class ApplicationProperties {

    private final RemoteApi remoteApi = new RemoteApi();

    public RemoteApi getRemoteApi() {
        return remoteApi;
    }

    public static class RemoteApi {
        String storageUrl;
        String producerUrl;

        public String getStorageUrl() {
            return storageUrl;
        }

        public void setStorageUrl(String storageUrl) {
            this.storageUrl = storageUrl;
        }

        public String getProducerUrl() {
            return producerUrl;
        }

        public void setProducerUrl(String producerUrl) {
            this.producerUrl = producerUrl;
        }
    }
}
