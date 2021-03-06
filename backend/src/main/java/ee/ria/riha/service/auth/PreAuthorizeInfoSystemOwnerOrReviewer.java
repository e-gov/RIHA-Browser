package ee.ria.riha.service.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link PreAuthorize} method access-control which allows access to users either with ROLE_HINDAJA role or users with
 * ROLE_KIRJELDAJA and owner code equal to the one that info system has. Access is controlled by {@link
 * InfoSystemAuthorizationService#isOwner(String)} method.
 *
 * @author Valentin Suhnjov
 * @see InfoSystemAuthorizationService#isOwner(String)
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@principalRoleCheckerService.hasRole('ROLE_HINDAJA')  or (@principalRoleCheckerService.hasRole('ROLE_KIRJELDAJA') and @infoSystemAuthorizationService.isOwner(#reference))")
public @interface PreAuthorizeInfoSystemOwnerOrReviewer {
}
