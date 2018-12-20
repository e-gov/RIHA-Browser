package ee.ria.riha.web;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.service.SecurityContextUtil;
import ee.ria.riha.service.UserService;
import ee.ria.riha.web.model.OrganizationModel;
import ee.ria.riha.web.model.UserDetailsModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/user")
@Api("Users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/organization")
    @ApiOperation("Change active organization of the current user")
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
        return getUserDetails();
    }

    @GetMapping
    @ApiOperation("Retrieves user details")
    public ResponseEntity<UserDetailsModel> getUserDetails() {
        return ResponseEntity.ok(createUserDetailsModel().orElse(null));
    }

    public Optional<UserDetailsModel> createUserDetailsModel() {
        return SecurityContextUtil.getUserDetails()
                .map(userDetails -> {
                    UserDetailsModel model = new UserDetailsModel();
                    setUserDetails(model);
                    setAuthorityDetails(model);
                    return model;
                });
    }

    private void setAuthorityDetails(UserDetailsModel model) {


        if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication() == null) {
            return;
        }

        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof RihaUserDetails)) {
            log.warn("principal is not of RihaUserDetail type, but instead {}", SecurityContextHolder.getContext().getAuthentication().getClass().getCanonicalName());
            return;
        }
        RihaUserDetails rihaUserDetails = (RihaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Roles are set regardless of authentication type
        SecurityContextUtil.getAuthentication()
                .map(RihaUserDetails::getAuthorities)
                .ifPresent(authorities ->
                        model.setRoles(authorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())));

        if (rihaUserDetails.getActiveOrganization() != null) {
            model.setActiveOrganization(createOrganizationModel(rihaUserDetails.getActiveOrganization()));
        }

        if (rihaUserDetails.getOrganizationAuthorities() != null) {
            rihaUserDetails.getOrganizationAuthorities().keySet()
                    .forEach(organization ->
                            model.getOrganizations().add(createOrganizationModel(organization)));
        }

    }

    private OrganizationModel createOrganizationModel(RihaOrganization rihaOrganization) {
        OrganizationModel organizationModel = new OrganizationModel();
        organizationModel.setCode(rihaOrganization.getCode());
        organizationModel.setName(rihaOrganization.getName());
        return organizationModel;
    }

    private void setUserDetails(UserDetailsModel model) {
        SecurityContextUtil.getRihaUserDetails()
                .ifPresent(userDetails -> {
                    model.setPersonalCode(userDetails.getPersonalCode());
                    model.setFirstName(userDetails.getFirstName());
                    model.setLastName(userDetails.getLastName());
                });
    }
}
