package ee.ria.riha.pages;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.driver.Wait;
import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver driver;
    protected Wait wait;

    public BasePage() {
        this.driver = Setup.driver;
        this.wait = new Wait(this.driver);
    }
}
