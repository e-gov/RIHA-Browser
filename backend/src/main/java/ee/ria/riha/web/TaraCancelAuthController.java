package ee.ria.riha.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TaraCancelAuthController {

    @GetMapping("/cancel-auth")
    public String cancelTaraAuth() {
        log.debug("somebody has just canceled an auth request");

        return "redirect:/";
    }
}
