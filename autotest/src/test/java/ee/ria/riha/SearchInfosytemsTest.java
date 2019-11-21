package ee.ria.riha;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/SearchInfosystems.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/search-infosystems-page.json",
        "html:target/search-infosystems-page-html"},
        glue = {"ee.ria.riha"})
public class SearchInfosytemsTest {
}
