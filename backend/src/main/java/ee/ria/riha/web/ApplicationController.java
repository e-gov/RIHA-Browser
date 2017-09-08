package ee.ria.riha.web;

import ee.ria.riha.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping("/")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping(value = "/")
    public ModelAndView index(Model model) {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/environment")
    public ResponseEntity environment() {
        return ResponseEntity.ok(applicationService.getEnvironment());
    }

    @GetMapping(value = "/login/esteid")
    public ResponseEntity estEIDLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null && authentication.getPrincipal() instanceof UserDetails;
        return ResponseEntity.status(authenticated ? HttpStatus.OK : HttpStatus.FORBIDDEN).build();
    }

    @PutMapping(value = "/login/organization")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity changeActiveOrganization(@RequestBody(required = false) String organizationCode) {
        applicationService.changeActiveOrganization(organizationCode);

        return environment();
    }
}
