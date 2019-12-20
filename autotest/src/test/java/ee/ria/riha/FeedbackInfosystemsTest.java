package ee.ria.riha;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/FeedbackInfosystems.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/feedback-infosystems-page.json",
        "html:target/feedback-infosystems-page-html"},
        glue = {"ee.ria.riha"})
public class FeedbackInfosystemsTest {
}
