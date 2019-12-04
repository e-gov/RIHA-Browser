package ee.ria.riha.infosystemmanagement;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/InfosystemManagement.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/infosystem-management-page.json",
        "html:target/infosystem-management-page-html"},
        glue = {"ee.ria.riha"})
public class InfosystemManagementTest {
}
