package ee.ria.riha.web;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.service.EnvironmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/environment")
@Api("Environment configuration")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private UserController userController;

    @Autowired
    private ApplicationProperties applicationProperties;

    @GetMapping
    @ApiOperation("Retrieve environment")
    public ResponseEntity environment(HttpSession session) {
        Map<String, Object> environment = new HashMap<>();
        environment.put("userDetails", userController.createUserDetailsModel());
        environment.put("tracking", applicationProperties.getTracking());
        environment.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval() * 1000);

        return ResponseEntity.ok(environment);
    }

    /**
     * @deprecated use user controller
     */
    @Deprecated
    @PutMapping("/organization")
    @ApiOperation("Change active organization of the current user")
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode, HttpSession session) {
        environmentService.changeActiveOrganization(organizationCode);
        return environment(session);
    }
}
