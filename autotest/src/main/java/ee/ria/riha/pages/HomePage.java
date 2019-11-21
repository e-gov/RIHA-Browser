package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;

public class HomePage extends BasePage {

    @FindBy(xpath = "//img[@src='assets/images/base/RIHA.png']")
    private WebElement logo;

    @FindBy(css = ".btn > span:nth-child(1)")
    private WebElement loginButton;

    @FindBy(css = ".card:nth-child(2) .btn")
    private WebElement goToMyInfosystemsPageButton;

    @FindBy(css = ".nav-item:nth-child(2) > .nav-link")
    private WebElement goToInfosystemsPageLink;

    @FindBy(tagName = "ngb-modal-window")
    private WebElement modalContainer;

    public HomePage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public void goToPage(String path) {
        String url = scenarioContext.getFromContext(ScenarioContext.APP_URL_KEY);
        driver.get(url + path);
        checkLogoDisplay();
    }

    public void checkLogoDisplay() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, this.logo, "Logo");
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getLoggedInUsersName() {
        return this.driver.findElement(By.cssSelector(".right a")).getText();
    }

    public void selectSecondOrganization() {
        wait.forPresenceOfElements(25, By.tagName("ngb-modal-window"), "modal");
        modalContainer.findElement(By.cssSelector("tr:nth-child(2) > td")).click();
    }

    public void selectOrganization(String organization) {
        driver.findElement(By.cssSelector(".right a")).click();
        wait.forPresenceOfElements(2, By.tagName("ngb-modal-window"), "modal");
        modalContainer.findElement(By.xpath("//td[contains(.,'" + organization +"')]")).click();
    }

    public void goToLoginPage() {
        loginButton.click();
    }

    public void goToMyInfosystemsPage() {
        goToMyInfosystemsPageButton.click();
    }

    public void goToInfosystemsPage() {
        goToInfosystemsPageLink.click();
    }
}