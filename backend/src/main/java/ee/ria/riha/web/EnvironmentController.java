package ee.ria.riha.web;

import ee.ria.riha.conf.*;
import ee.ria.riha.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.servlet.http.*;

import static ee.ria.riha.conf.ApplicationProperties.*;

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
        environment.put("userDetails", userController.createUserDetailsModel().orElse(null));
        environment.put("tracking", applicationProperties.getTracking());
        ApplicationProperties.FeedbackRecaptcha feedbackRecaptcha = applicationProperties.getFeedbackRecaptcha();

        Map<String, String> recaptchaProperties = new HashMap<>();
        recaptchaProperties.put("siteKey", feedbackRecaptcha.getSiteKey());
        recaptchaProperties.put("enabled", feedbackRecaptcha.isEnabled() + "");

        environment.put("feedbackRecaptcha", recaptchaProperties);
        environment.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval() * 1000);

        return ResponseEntity.ok(environment);
    }

    @GetMapping("/classifiers")
    @ApiOperation("Load classifiers")
    public ResponseEntity classifiers() {
        return ResponseEntity.ok(environmentService.getClassifiers());
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
