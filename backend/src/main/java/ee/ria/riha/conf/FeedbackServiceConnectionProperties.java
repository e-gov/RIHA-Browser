package ee.ria.riha.conf;

import lombok.*;
import org.springframework.boot.context.properties.*;

import java.io.*;

@ConfigurationProperties(prefix = "feedback.service")
@Getter
@Setter
public class FeedbackServiceConnectionProperties {


    private File trustStore;
    private char[] trustStorePassword;

    private File keyStore;
    private char[] keyStorePassword;
    private String keyStoreAlias;
    private char[] keyStoreKeyPassword;

    private String protocol;

    private String url;

}
