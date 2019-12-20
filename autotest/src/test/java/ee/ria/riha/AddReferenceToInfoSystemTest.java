package ee.ria.riha;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/AddReferenceToInfoSystem.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/add-reference-to-infosystem.json",
        "html:target/add-reference-to-infosystem-html"},
        glue = {"ee.ria.riha"})
public class AddReferenceToInfoSystemTest {
}
