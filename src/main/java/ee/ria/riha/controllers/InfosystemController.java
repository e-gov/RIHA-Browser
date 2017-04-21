package ee.ria.riha.controllers;

import ee.ria.riha.models.Infosystem;
import ee.ria.riha.services.InfosystemStorageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

  @RequestMapping(value = "/detail/{ownerCode}/{shortname}", method = RequestMethod.GET)
  public String detailView(Model model, @PathVariable("shortname") String shortname, @PathVariable("ownerCode") String ownerCode) {
    model.addAttribute("shortname", shortname);
    model.addAttribute("ownerCode", ownerCode);
    model.addAttribute("infosystemsUrl", infosystemsUrl);
    return "detailView";
  }



}
