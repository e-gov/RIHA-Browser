package ee.ria.riha.service.auth;

import ee.ria.riha.authentication.RihaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PrincipalRoleCheckerService {


    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (role == null || !(authentication.getPrincipal() instanceof RihaUserDetails) || ((RihaUserDetails) authentication.getPrincipal()).getAuthorities() == null) {
            return false;
        }

        return ((RihaUserDetails) authentication.getPrincipal()).getAuthorities().stream()
                .anyMatch(authority -> role.equals(authority.getAuthority()));

    }
}
