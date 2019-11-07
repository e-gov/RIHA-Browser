package ee.ria.riha.pages;

import ee.ria.riha.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    @FindBy(xpath = "//img[@src='assets/images/base/RIHA.png']")
    private WebElement logo;

    @FindBy(css = ".btn > span:nth-child(1)")
    private WebElement loginButton;

    @FindBy(css = ".card:nth-child(2) .btn")
    private WebElement goToMyInfosystemsPageButton;

    @FindBy(tagName = "ngb-modal-window")
    private WebElement modalContainer;

    public HomePage() {
        PageFactory.initElements(driver, this);
    }

    public void goToHomePage(String url) {
        driver.get(url);
        wait.forLoading(5);
    }

    public void checkLogoDisplay() {
        wait.forElementToBeDisplayed(5, this.logo, "Logo");
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getLoggedInUsersName() {
        return this.driver.findElement(By.cssSelector(".right a")).getText();
    }

    public void selectSecondOrganization() {
        this.wait.forPresenceOfElements(25, By.tagName("ngb-modal-window"), "modal");
        this.modalContainer.findElement(By.cssSelector("tr:nth-child(2) > td")).click();
    }

    public void selectOrganization(String organization) {
        this.driver.findElement(By.cssSelector(".right a")).click();
        this.wait.forPresenceOfElements(2, By.tagName("ngb-modal-window"), "modal");
        this.modalContainer.findElement(By.xpath("//td[contains(.,'" + organization +"')]")).click();
    }

    public void goToLoginPage() {
        this.loginButton.click();
        wait.forLoading(5);
    }

    public void goToMyInfosystemsPage() {
        this.goToMyInfosystemsPageButton.click();
        wait.forLoading(5);
    }
}