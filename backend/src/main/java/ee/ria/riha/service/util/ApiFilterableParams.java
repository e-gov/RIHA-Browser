package ee.ria.riha.service.util;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Parameter(
      name = "filter",
      description = "filter options",
      in = ParameterIn.QUERY,
      schema = @Schema(type = "string")
    ),
    @Parameter(
      name = "sort",
      description = "sorting options",
      in = ParameterIn.QUERY,
      schema = @Schema(type = "string")
    ),
    @Parameter(
      name = "fields",
      description = "field list",
      in = ParameterIn.QUERY,
      schema = @Schema(type = "string")
    )
    })
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFilterableParams {}
