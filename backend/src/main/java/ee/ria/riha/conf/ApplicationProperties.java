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

    private final StorageClientProperties storageClient = new StorageClientProperties();
    private final AuthenticationProperties authentication = new AuthenticationProperties();
    private final ValidationProperties validation = new ValidationProperties();
    private final NotificationProperties notification = new NotificationProperties();
    private final CorsProperties cors = new CorsProperties();
    private final TrackingProperties tracking = new TrackingProperties();

    @Setter
    private String baseUrl;

    @Getter
    @Setter
    public static class StorageClientProperties {
        @NotEmpty
        private String baseUrl;
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

    @Getter
    @Setter
    public static class NotificationProperties {
        private final CreatedInfoSystemsOverview createdInfoSystemsOverview = new CreatedInfoSystemsOverview();
        private final NewIssue newIssue = new NewIssue();
        private String from;
    }

    @Getter
    @Setter
    public static class NewIssue {
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class CreatedInfoSystemsOverview {
        private String[] to;
        private String[] cc;
        private String[] bcc;
    }

    @Getter
    @Setter
    public static class CorsProperties {
        private String[] allowedOrigins;
    }

    @Getter
    @Setter
    public static class TrackingProperties {
        private final GoogleAnalyticsProperties googleAnalytics = new GoogleAnalyticsProperties();
        private final HotjarProperties hotjar = new HotjarProperties();
    }

    @Getter
    @Setter
    public static class GoogleAnalyticsProperties {
        private String id;
    }

    @Getter
    @Setter
    public static class HotjarProperties {
        private String hjid;
        private String hjsv;
    }


}
