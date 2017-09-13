package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.web.model.OrganizationModel;
import ee.ria.riha.web.model.UserDetailsModel;
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
public class EnvironmentService {

    @Autowired
    private ApplicationProperties applicationProperties;

    public Map<String, Object> getEnvironment() {
        HashMap<String, Object> environment = new HashMap<>();

        environment.put("remotes", applicationProperties.getRemoteApi());
        environment.put("userDetails", createUserDetailsModel());

        return environment;
    }

    private UserDetailsModel createUserDetailsModel() {
        UserDetails userDetails = SecurityContextUtil.getUserDetails();

        if (userDetails == null) {
            return null;
        }

        UserDetailsModel model = new UserDetailsModel();

        model.setRoles(userDetails.getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList()));

        if (userDetails instanceof RihaUserDetails) {
            RihaUserDetails rihaUserDetails = (RihaUserDetails) userDetails;
            model.setPersonalCode(rihaUserDetails.getPersonalCode());
            model.setFirstName(rihaUserDetails.getFirstName());
            model.setLastName(rihaUserDetails.getLastName());

            for (RihaOrganization rihaOrganization : rihaUserDetails.getOrganizationRoles().keySet()) {
                OrganizationModel organizationModel = createOrganizationModel(rihaOrganization);

                organizationModel.getRoles().addAll(
                        rihaUserDetails.getOrganizationRoles().get(rihaOrganization).stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()));

                model.getOrganizations().add(organizationModel);
            }

            RihaOrganization activeOrganization = rihaUserDetails.getActiveOrganization();
            model.setActiveOrganization(activeOrganization != null
                                                ? createOrganizationModel(activeOrganization)
                                                : null);
        }

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
