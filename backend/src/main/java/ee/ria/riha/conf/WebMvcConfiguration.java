package ee.ria.riha.conf;

import ee.ria.riha.storage.util.CompositeFilterArgumentResolver;
import ee.ria.riha.storage.util.FilterableArgumentResolver;
import ee.ria.riha.storage.util.PageableArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    @Autowired
    public WebMvcConfiguration(ApplicationProperties applicationProperties) {
        allowedOrigins = applicationProperties.getCors().getAllowedOrigins();
        Assert.notEmpty(allowedOrigins, "Allowed origins must be defined.");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableArgumentResolver());
        argumentResolvers.add(new FilterableArgumentResolver());
        argumentResolvers.add(new CompositeFilterArgumentResolver());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        configureFontCacheControl(registry);
        registry.addResourceHandler("/infosystem_schema.json").addResourceLocations("classpath:/infosystem_schema.json");
    }

    /**
     * Fixes bug when IE does not load fonts over SSL when Cache-Control header is set to "no-cache".
     *
     * @param registry resource handler registry for configuration
     */
    private void configureFontCacheControl(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.{(eot|woff|woff2|ttf|svg)}")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));
    }

    static class UUIDConverter implements Converter<String, UUID> {
        @Override
        public UUID convert(String source) {
            return UUID.fromString(source);
        }
    }
}