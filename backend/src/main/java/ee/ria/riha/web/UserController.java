package ee.ria.riha.web;

import ee.ria.riha.service.UserService;
import ee.ria.riha.web.model.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserDetailsModel> getUserDetails() {
        return ResponseEntity.ok(userService.getUserDetails());
    }

    @PutMapping("/organization")
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
        return getUserDetails();
    }
}
