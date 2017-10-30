package ee.ria.riha.conf;

import ee.ria.riha.storage.util.FilterableArgumentResolver;
import ee.ria.riha.storage.util.PageableArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    private final String[] allowedOrigins;

    @Autowired
    public WebMvcConfiguration(ApplicationProperties applicationProperties) {
        allowedOrigins = applicationProperties.getCors().getAllowedOrigins();
        Assert.notEmpty(allowedOrigins, "Allowed origins must be defined.");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new PageableArgumentResolver());
        argumentResolvers.add(new FilterableArgumentResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new UUIDConverter());
    }

    @Bean
    public ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
        return (request, status, model) -> status == HttpStatus.NOT_FOUND
                ? new ModelAndView("index.html", Collections.emptyMap(), HttpStatus.OK)
                : null;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    static class UUIDConverter implements Converter<String, UUID> {
        @Override
        public UUID convert(String source) {
            return UUID.fromString(source);
        }
    }
}