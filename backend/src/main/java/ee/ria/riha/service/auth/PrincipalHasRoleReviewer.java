package ee.ria.riha.service.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * {@link PreAuthorize} method access-control which allows access to reviewer role
 *
 * @author Aleksandr Ivanov
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@principalRoleCheckerService.hasRole('ROLE_HINDAJA')")
public @interface PrincipalHasRoleReviewer {}
