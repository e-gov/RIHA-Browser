package ee.ria.riha.conf;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@ConfigurationProperties(prefix = "browser")
@Getter
public class ApplicationProperties {

    public static final String API_V1_PREFIX = "/api/v1";

    private final RemoteApi remoteApi = new RemoteApi();
    private final StorageClientProperties storageClient = new StorageClientProperties();
    private final AuthenticationProperties authentication = new AuthenticationProperties();
    private final ValidationProperties validation = new ValidationProperties();

    @Getter
    @Setter
    public static class StorageClientProperties {
        @NotEmpty
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class RemoteApi {
        private String approverUrl;
    }

    @Getter
    @Setter
    public static class AuthenticationProperties {
        private String userSearchBase;
        private String userSearchFilter;
        private String ldapUrl;
        private String ldapBaseDn;
        private String ldapUser;
        private String ldapPassword;
    }

    @Getter
    @Setter
    public static class ValidationProperties {
        private String jsonSchemaUrl;
    }
}