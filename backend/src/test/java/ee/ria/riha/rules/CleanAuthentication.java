package ee.ria.riha.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Clears {@link SecurityContextHolder} authentication before and after base statement evaluation.
 *
 * @author Valentin Suhnjov
 */
public class CleanAuthentication implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return statement(base);
    }

    private Statement statement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                clearAuthentication();
                try {
                    base.evaluate();
                } finally {
                    clearAuthentication();
                }
            }
        };
    }

    private void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
