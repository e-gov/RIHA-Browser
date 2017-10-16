package ee.ria.riha.conf;

import com.github.fge.jackson.JsonLoader;
import ee.ria.riha.service.JsonValidationService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public JsonValidationService jsonValidationService(ApplicationProperties applicationProperties) throws IOException {
        return new JsonValidationService(
                JsonLoader.fromResource(applicationProperties.getValidation().getJsonSchemaUrl()));
    }
}
