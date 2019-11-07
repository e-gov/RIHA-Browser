package ee.ria.riha.myinfosystemspage;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/MyInfosystems.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/my-infosystems-page.json",
        "html:target/my-infosystems-page-html"},
        glue = {"ee.ria.riha"})
public class MyInfosystemsPageTest {
}
