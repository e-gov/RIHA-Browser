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

    private String storageUrl;

    public RemoteApi getRemoteApi() {
        return remoteApi;
    }

    public String getStorageUrl() {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl) {
        this.storageUrl = storageUrl;
    }


    public static class RemoteApi {
        private String producerUrl;

        public String getProducerUrl() {
            return producerUrl;
        }

        public void setProducerUrl(String producerUrl) {
            this.producerUrl = producerUrl;
        }
    }
}
