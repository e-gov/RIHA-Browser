package ee.ria.riha.pages;

import ee.ria.riha.context.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.*;

import static ee.ria.riha.Timeouts.*;

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

    @FindBy(xpath = "//input[@id='search']")
    private WebElement searchBarInput;

    @FindBy(xpath = "//a[contains(text(),'Hinda')]")
    private WebElement evaluateButton;

    @FindBy(xpath = "//header[@id='header']/div[2]/app-riha-navbar/div/div/div[2]/div/span/a")
    private WebElement selectOrganizationButton;


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
        wait.forPresenceOfElements(25, By.tagName("ngb-modal-window"), "modal");
        modalContainer.findElement(By.xpath("//td[text()='"+organization+"']")).click();
    }

    public void goToLoginPage() {
        driver.navigate().refresh();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, loginButton, "loginButton");
        loginButton.click();
    }

    public WebElement getModalContainer() {
        return modalContainer;
    }

    public void goToMyInfosystemsPage() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, goToMyInfosystemsPageButton, "goToMyInfosystemsPageButton");
        goToMyInfosystemsPageButton.click();
    }

    public void goToInfosystemsPage() {
        goToInfosystemsPageLink.click();
    }

    public void inputSearchTerm(String word) {
        this.searchBarInput.sendKeys(word);
        this.searchBarInput.sendKeys(Keys.RETURN);
    }

    public void clickEvaluate() {
        driver.navigate().refresh();
        wait.forElementToBeClickable(DISPLAY_ELEMENT_TIMEOUT, By.xpath("//a[contains(text(),'Hinda')]"), "evaluateButton");
        this.evaluateButton.click();
    }

    public void openSelectOrganizationDialog() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, selectOrganizationButton, "selectOrganizationButton");
        selectOrganizationButton.click();
    }

}