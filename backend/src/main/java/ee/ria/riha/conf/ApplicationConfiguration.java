package ee.ria.riha.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jackson.JsonLoader;

import ee.ria.riha.domain.model.NationalHolidays;
import ee.ria.riha.service.JsonValidationService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Valentin Suhnjov
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableScheduling
@EnableAsync
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public TaskExecutor taskExecutor(TaskExecutorBuilder taskExecutorBuilder) {
        return taskExecutorBuilder.build();
    }

    @Bean
    public JsonValidationService jsonValidationService(ApplicationProperties applicationProperties) throws IOException {
        return new JsonValidationService(
                JsonLoader.fromResource(applicationProperties.getValidation().getJsonSchemaUrl()));
    }

    @Bean
    public NationalHolidays nationalHolidays(ApplicationProperties applicationProperties) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        File holidaysFile = new File(getClass().getClassLoader().getResource(applicationProperties.getNationalHolidaysFile()).toURI());
        return mapper.readValue(holidaysFile, NationalHolidays.class);
    }
}
