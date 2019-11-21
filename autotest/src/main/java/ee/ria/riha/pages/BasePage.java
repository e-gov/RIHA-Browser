package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import ee.ria.riha.driver.Setup;
import ee.ria.riha.driver.Wait;
import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver driver;
    protected Wait wait;
    protected ScenarioContext scenarioContext;

    public BasePage(ScenarioContext scenarioContext) {
        this.driver = Setup.driver;
        this.wait = new Wait(driver);
        this.scenarioContext = scenarioContext;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }
}