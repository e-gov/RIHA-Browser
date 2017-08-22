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

    private final RemoteApi remoteApi = new RemoteApi();
    private final StorageClientProperties storageClient = new StorageClientProperties();

    public RemoteApi getRemoteApi() {
        return remoteApi;
    }

    public StorageClientProperties getStorageClient() {
        return storageClient;
    }

    public static class StorageClientProperties {

        @NotEmpty
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class RemoteApi {
        private String producerUrl;
        private String approverUrl;

        public String getProducerUrl() {
            return producerUrl;
        }

        public void setProducerUrl(String producerUrl) {
            this.producerUrl = producerUrl;
        }

        public String getApproverUrl() {
            return approverUrl;
        }

        public void setApproverUrl(String approverUrl) {
            this.approverUrl = approverUrl;
        }
    }
}
