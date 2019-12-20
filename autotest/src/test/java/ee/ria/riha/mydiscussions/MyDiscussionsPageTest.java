package ee.ria.riha.mydiscussions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/MyDiscussions.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/my-discussions-page.json",
        "html:target/my-discussions-page-html"},
        glue = {"ee.ria.riha"})
public class MyDiscussionsPageTest {
}
