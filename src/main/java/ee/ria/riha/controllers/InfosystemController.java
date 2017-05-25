package ee.ria.riha.controllers;

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

  @Value("${conf.url")
  private String confUrl;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(Model model) {
    model.addAttribute("infosystemsUrl", infosystemsUrl);
    return "index";
  }

  @RequestMapping(value = "/inf/{ownerCode}/{shortname}", method = RequestMethod.GET)
  public String detailView(Model model, @PathVariable("shortname") String shortname, @PathVariable("ownerCode") int ownerCode) {
    model.addAttribute("shortname", shortname);
    model.addAttribute("ownerCode", ownerCode);
    model.addAttribute("infosystemsUrl", infosystemsUrl);
    return "detailView";
  }

    @RequestMapping(value = "/asu/{ownerCode}", method = RequestMethod.GET)
    public String detailView(Model model, @PathVariable("ownerCode") int ownerCode) {
        model.addAttribute("ownerCode", ownerCode);
        return "Infosystemlist";
    }


}
