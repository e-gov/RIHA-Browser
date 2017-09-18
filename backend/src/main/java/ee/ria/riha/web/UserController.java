package ee.ria.riha.web;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.service.SecurityContextUtil;
import ee.ria.riha.service.UserService;
import ee.ria.riha.web.model.OrganizationModel;
import ee.ria.riha.web.model.UserDetailsModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;
import static ee.ria.riha.service.SecurityContextUtil.isUserAuthenticated;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/user")
@Api("Users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/organization")
    @ApiOperation("Change active organization of the current user")
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
        return ResponseEntity.ok(createUserDetailsModel());
    }

    @GetMapping
    @ApiOperation("Retrieves user details")
    public ResponseEntity<UserDetailsModel> getUserDetails() {
        return ResponseEntity.ok(createUserDetailsModel());
    }

    private void setUserDetails(UserDetailsModel model) {
        RihaUserDetails userDetails = SecurityContextUtil.getRihaUserDetails();
        if (userDetails == null) {
            return;
        }

        model.setPersonalCode(userDetails.getPersonalCode());
        model.setFirstName(userDetails.getFirstName());
        model.setLastName(userDetails.getLastName());
    }

    public UserDetailsModel createUserDetailsModel() {
        if (!isUserAuthenticated()) {
            return null;
        }

        UserDetailsModel model = new UserDetailsModel();
        setUserDetails(model);
        setAuthorityDetails(model);

        return model;
    }

    private void setAuthorityDetails(UserDetailsModel model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.setRoles(authentication.getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList()));

        if (authentication instanceof RihaOrganizationAwareAuthenticationToken) {
            RihaOrganizationAwareAuthenticationToken rihaAuthentication = (RihaOrganizationAwareAuthenticationToken) authentication;
            model.setActiveOrganization(createOrganizationModel(rihaAuthentication.getActiveOrganization()));

            for (RihaOrganization rihaOrganization : rihaAuthentication.getOrganizationAuthorities().keySet()) {
                model.getOrganizations().add(createOrganizationModel(rihaOrganization));
            }
        }
    }

    private OrganizationModel createOrganizationModel(RihaOrganization rihaOrganization) {
        if (rihaOrganization == null) {
            return null;
        }

        OrganizationModel organizationModel = new OrganizationModel();
        organizationModel.setCode(rihaOrganization.getCode());
        organizationModel.setName(rihaOrganization.getName());
        return organizationModel;
    }
}
