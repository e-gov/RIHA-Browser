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
    private final LdapProperties ldap = new LdapProperties();
    private final LdapAuthenticationProperties ldapAuthentication = new LdapAuthenticationProperties();
    private final LdapRepositoryProperties ldapRepository = new LdapRepositoryProperties();
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
    public static class LdapProperties {
        private String url;
        private String baseDn;
        private String user;
        private String password;
    }

    @Getter
    @Setter
    public static class LdapAuthenticationProperties {
        private String userSearchBase;
        private String userSearchFilter;
    }

    @Getter
    @Setter
    public static class LdapRepositoryProperties {
        private String userSearchBase;
        private String groupSearchBase;
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
        private final NewIssueComment newIssueComment = new NewIssueComment();
        private String from;
    }

    @Getter
    @Setter
    public static class NewIssue {
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class NewIssueComment {
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
