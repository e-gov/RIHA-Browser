package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.model.OrganizationModel;
import ee.ria.riha.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

            for (RihaOrganization rihaOrganization : userDetails.getOrganizationRoles().keySet()) {
                OrganizationModel organizationModel = createOrganizationModel(rihaOrganization);

                organizationModel.getRoles().addAll(userDetails.getOrganizationRoles().get(rihaOrganization).stream()
                                                            .map(GrantedAuthority::getAuthority)
                                                            .collect(Collectors.toList()));

                model.getOrganizations().add(organizationModel);
            }

            RihaOrganization activeOrganization = userDetails.getActiveOrganization();
            model.setActiveOrganization(activeOrganization != null
                                                ? createOrganizationModel(activeOrganization)
                                                : null);
        }

        model.setRoles(((UserDetails) principal).getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList()));
        return model;
    }

    private OrganizationModel createOrganizationModel(RihaOrganization rihaOrganization) {
        OrganizationModel organizationModel = new OrganizationModel();

        organizationModel.setCode(rihaOrganization.getCode());
        organizationModel.setName(rihaOrganization.getName());

        return organizationModel;
    }

    public void changeActiveOrganization(String organizationCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(authentication, "authentication not found in security context");

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof RihaUserDetails)) {
            throw new IllegalStateException("User details do not support organization change");
        }

        ((RihaUserDetails) principal).setActiveOrganization(organizationCode);
    }
}
