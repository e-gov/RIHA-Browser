package ee.ria.riha.service.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link PreAuthorize} method access-control which allows access to users with both ROLE_KIRJELDAJA role and owner code
 * equal to the one that info system has. Access is controlled by {@link InfoSystemAuthorizationService#isOwner(String)}
 * method.
 *
 * @author Valentin Suhnjov
 * @see InfoSystemAuthorizationService#isOwner(String)
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@principalRoleCheckerService.hasRole('ROLE_KIRJELDAJA') and @infoSystemAuthorizationService.isOwner(#reference)")
public @interface PreAuthorizeInfoSystemOwner {
}
