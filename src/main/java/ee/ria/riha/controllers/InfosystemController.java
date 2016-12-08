package ee.ria.riha.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InfosystemController {

  @Value("${infosystems.url}")
  private String infosystemsUrl;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(Model model) {
    model.addAttribute("infosystemsUrl", infosystemsUrl);
    return "index";
  }
}
