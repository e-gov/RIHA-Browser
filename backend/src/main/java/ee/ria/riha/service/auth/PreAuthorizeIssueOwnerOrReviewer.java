package ee.ria.riha.service.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link PreAuthorize} method access-control which allows access to users either with ROLE_HINDAJA role or users with
 * ROLE_KIRJELDAJA role and owner code equal to the one that info system has. Access is controlled by {@link
 * IssueAuthorizationService#isIssueOwner(Long)} method.
 *
 * @author Valentin Suhnjov
 * @see IssueAuthorizationService#isIssueOwner(Long)
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@principalRoleCheckerService.hasRole('ROLE_HINDAJA')  or (@principalRoleCheckerService.hasRole('ROLE_KIRJELDAJA') and @issueAuthorizationService.isIssueOwner(#issueId))")
public @interface PreAuthorizeIssueOwnerOrReviewer {
}
