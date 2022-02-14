package ee.ria.riha.service.util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents query parameters of {@link Filterable} type resolved by {@link FilterableArgumentResolver}
 *
 * @author Valentin Suhnjov
 * @see ApiImplicitParam
 * @see ApiImplicitParams
 */
@ApiImplicitParams(value = {
        @ApiImplicitParam(name = "filter", value = "filter options", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "sort", value = "sorting options", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "fields", value = "field list", dataType = "string", paramType = "query")
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFilterableParams {
}
