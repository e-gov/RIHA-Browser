package ee.ria.riha.myorganization;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/ee/ria/riha/MyOrganization.feature"},
        strict = false, plugin = {"pretty",
        "json:target/cucumber_json_reports/my-organization-page.json",
        "html:target/my-organization-page-html"},
        glue = {"ee.ria.riha"})
public class MyOrganizationPageTest {
}
