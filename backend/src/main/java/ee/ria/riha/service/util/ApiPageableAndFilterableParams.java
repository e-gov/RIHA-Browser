package ee.ria.riha.service.util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents combination of {@link ApiPageableParams} and {@link ApiFilterableParams}
 *
 * @author Valentin Suhnjov
 * @see ApiPageableParams
 * @see ApiFilterableParams
 */
@ApiImplicitParams(value = {
        @ApiImplicitParam(name = "page", value = "page number", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "page size", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "filter", value = "filter options", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "sort", value = "sorting options", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "fields", value = "field list", dataType = "string", paramType = "query")
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPageableAndFilterableParams {
}
