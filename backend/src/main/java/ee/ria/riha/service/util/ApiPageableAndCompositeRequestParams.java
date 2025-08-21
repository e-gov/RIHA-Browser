package ee.ria.riha.service.util;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents combination of {@link ApiPageableParams} and {@link CompositeFilterRequest}
 *
 * @author Valentin Suhnjov
 * @see ApiPageableParams
 */
@Parameters(
    value = {
      @Parameter(name = "page", description = "page number"),
      @Parameter(name = "size", description = "page size"),
      @Parameter(name = "filter", description = "filter options"),
      @Parameter(name = "sort", description = "sorting options")
    })
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPageableAndCompositeRequestParams {}
