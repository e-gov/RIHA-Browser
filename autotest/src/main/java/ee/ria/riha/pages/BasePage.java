package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import ee.ria.riha.driver.Setup;
import ee.ria.riha.driver.Wait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BasePage {
    protected WebDriver driver;
    protected Wait wait;
    protected ScenarioContext scenarioContext;

    public BasePage(ScenarioContext scenarioContext) {
        this.driver = Setup.driver;
        this.wait = new Wait(driver);
        this.scenarioContext = scenarioContext;
    }

    public String getCurrentUrl(boolean decodeUrl) {
        if (!decodeUrl) {
            return driver.getCurrentUrl();
        }

        try {
            return URLDecoder.decode(driver.getCurrentUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    public Wait getWait() {
        return wait;
    }

    public void makeElementVisible(String id) {
        String changeVisibilityScript = "$(\"#" + id + "\").css(\"visibility\",\"visible\");";
        String changeDisplayScript = "$(\"#" + id + "\").css(\"display\",\"block\");";

        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        jsExecutor.executeScript(changeVisibilityScript);
        jsExecutor.executeScript(changeDisplayScript);
    }

    public void goHistoryMinusOne() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        jsExecutor.executeScript("window.history.go(-1)");
        wait.sleep(3000);
    }
}
