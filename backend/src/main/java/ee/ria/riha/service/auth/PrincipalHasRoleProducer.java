package ee.ria.riha.service.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link PreAuthorize} method access-control which allows access to producer role
 *
 * @author Aleksandr Ivanov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@principalRoleCheckerService.hasRole('ROLE_KIRJELDAJA')")
public @interface PrincipalHasRoleProducer {
}
