package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.model.OrganizationModel;
import ee.ria.riha.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
        
        if (principal instanceof RihaUserDetails) {
            RihaUserDetails userDetails = (RihaUserDetails) principal;

            model.setPersonalCode(userDetails.getPersonalCode());
            model.setFirstName(userDetails.getFirstName());
            model.setLastName(userDetails.getLastName());

            for (Map.Entry<RihaOrganization, List<String>> entry : userDetails.getOrganizations().entrySet()) {
                OrganizationModel organizationModel = new OrganizationModel();

                RihaOrganization rihaOrganization = entry.getKey();

                organizationModel.setCode(rihaOrganization.getCode());
                organizationModel.setName(rihaOrganization.getName());
                organizationModel.getRoles().addAll(entry.getValue());

                model.getOrganizations().add(organizationModel);
            }
        }

        return model;
    }
}
