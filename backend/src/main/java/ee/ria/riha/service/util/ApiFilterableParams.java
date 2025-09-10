package ee.ria.riha.service.util;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents query parameters of {@link Filterable} type resolved by {@link
 * FilterableArgumentResolver}
 *
 * @author Valentin Suhnjov
 * @see Parameter
 * @see Parameters
 */
@Parameters(
    value = {
      @Parameter(name = "filter", description = "filter options"),
      @Parameter(name = "sort", description = "sorting options"),
      @Parameter(name = "fields", description = "field list")
    })
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFilterableParams {}
