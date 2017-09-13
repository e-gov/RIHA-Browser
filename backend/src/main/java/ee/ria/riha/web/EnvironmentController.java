package ee.ria.riha.web;

import ee.ria.riha.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @GetMapping
    public ResponseEntity environment() {
        return ResponseEntity.ok(environmentService.getEnvironment());
    }

    @PutMapping("/organization")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        environmentService.changeActiveOrganization(organizationCode);
        return environment();
    }

}
