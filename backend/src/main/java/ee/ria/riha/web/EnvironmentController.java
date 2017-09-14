package ee.ria.riha.web;

import ee.ria.riha.service.EnvironmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @ApiOperation("Retrieve environment")
    public ResponseEntity environment() {
        return ResponseEntity.ok(environmentService.getEnvironment());
    }

    /**
     * @deprecated use user controller
     */
    @Deprecated
    @PutMapping("/organization")
    @ApiOperation("Change active organization of the current user")
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        environmentService.changeActiveOrganization(organizationCode);
        return environment();
    }
}
