package ee.ria.riha.loginpage;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/Login.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/login.json",
        "html:target/login-html"},
        glue = {"ee.ria.riha.driver",
                "ee.ria.riha.homepage",
                "ee.ria.riha.loginpage"})
public class LoginPageTest {
}
