package ee.ria.riha.web;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to provide CSRF token for frontend applications. This is required for Spring Security
 * 6.x compatibility.
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/csrf")
public class CsrfController {

  @GetMapping("/token")
  public Map<String, String> getCsrfToken(HttpServletRequest request) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (csrfToken != null) {
      Map<String, String> response = new HashMap<>();
      response.put("token", csrfToken.getToken());
      response.put("headerName", csrfToken.getHeaderName());
      response.put("parameterName", csrfToken.getParameterName());
      return response;
    }
    return new HashMap<>();
  }
}
