package ee.ria.riha.rules;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Clears {@link SecurityContextHolder} authentication before and after test execution.
 *
 * @author Valentin Suhnjov
 */
public class CleanAuthentication implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    clearAuthentication();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    clearAuthentication();
  }

  private void clearAuthentication() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
