package ee.ria.riha.conf;

import java.io.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.*;

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
