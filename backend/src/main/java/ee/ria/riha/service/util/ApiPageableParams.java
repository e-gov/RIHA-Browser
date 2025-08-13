package ee.ria.riha.service.util;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents query parameters of {@link Pageable} type resolved by {@link PageableArgumentResolver}
 *
 * @author Valentin Suhnjov
 * @see Parameter
 * @see Parameters
 */
@Parameters(value = {
        @Parameter(name = "page", description = "page number"),
        @Parameter(name = "size", description = "page size")
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPageableParams {
}
