package ee.ria.riha.pages;

import ee.ria.riha.context.*;
import ee.ria.riha.driver.*;
import org.openqa.selenium.*;

import java.io.*;
import java.net.*;

public class BasePage {
    protected WebDriver driver;
    protected static Wait wait;
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

    public boolean isLeftMenuOptionVisible(String menuOptionText) {
        return driver
                .findElement(By.xpath("//nav[@id='sidemenu']//a[contains(.,'" + menuOptionText + "')]"))
                .isDisplayed();
    }

    public boolean checkIfTableWithHeadersIsVisible(String commaSeparatedHeaderNames) {
        wait.sleep(3000);
        for (String headerLabel : commaSeparatedHeaderNames.split(",")) {
            if (!driver
                    .findElement(By.xpath("//table/thead/th[contains(.,'" + headerLabel.trim() + "')]"))
                    .isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    public void rememberSystemNameAndShortName(String systemName, String shortName) {
        scenarioContext.saveToContext(ScenarioContext.CREATED_SYSTEM_NAME, systemName);
        scenarioContext.saveToContext(ScenarioContext.CREATED_SYSTEM_SHORT_NAME, shortName);
    }
}
