package ee.ria.riha.web;

import ee.ria.riha.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static ee.ria.riha.service.SecurityContextUtil.isUserAuthenticated;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping("/")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping(value = "/environment")
    public ResponseEntity environment() {
        return ResponseEntity.ok(applicationService.getEnvironment());
    }

    @PutMapping(value = "/environment/organization")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        applicationService.changeActiveOrganization(organizationCode);
        return environment();
    }

    @GetMapping(value = "/login/esteid")
    public ResponseEntity estEIDLogin() {
        return ResponseEntity.status(isUserAuthenticated() ? HttpStatus.OK : HttpStatus.FORBIDDEN).build();
    }
}
