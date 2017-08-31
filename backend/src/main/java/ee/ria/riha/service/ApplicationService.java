package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentin Suhnjov
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationProperties applicationProperties;

    public Map<String, Object> getEnvironment() {
        HashMap<String, Object> environment = new HashMap<>();

        environment.put("remotes", applicationProperties.getRemoteApi());
        environment.put("userDetails", createUserDetailsModel());

        return environment;
    }

    private UserDetailsModel createUserDetailsModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }

        UserDetailsModel model = new UserDetailsModel();
        authentication.getAuthorities().forEach(authority -> model.getRoles().add(authority.getAuthority()));

        if (principal instanceof RihaUserDetails) {
            RihaUserDetails userDetails = (RihaUserDetails) principal;
            model.setPersonalCode(userDetails.getPersonalCode());
            model.setFirstName(userDetails.getFirstName());
            model.setLastName(userDetails.getLastName());
            model.getOrganizations().addAll(userDetails.getOrganizations());
        }

        return model;
    }
}
