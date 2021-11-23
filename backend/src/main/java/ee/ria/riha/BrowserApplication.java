package ee.ria.riha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.mail.MailHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {
        LdapAutoConfiguration.class,
    MailHealthContributorAutoConfiguration.class
})
public class BrowserApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BrowserApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BrowserApplication.class);
    }
}
