package ee.ria.riha.web;

import ee.ria.riha.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

}
