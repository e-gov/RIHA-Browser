package ee.ria.riha.homepage;

import ee.ria.riha.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    @FindBy(xpath = "//img[@src='assets/images/base/RIHA.png']")
    private WebElement logo;

    @FindBy(css = ".btn > span:nth-child(1)")
    private WebElement loginButton;

    @FindBy(tagName = "ngb-modal-window")
    private WebElement modalContainer;

    HomePage() {
        PageFactory.initElements(driver, this);
    }

    void goToHomePage(String url) {
        driver.get(url);
        wait.forLoading(5);
    }

    void checkLogoDisplay() {
        wait.forElementToBeDisplayed(5, this.logo, "Logo");
    }

    String getTitle() {
        return driver.getTitle();
    }

    String getLoggedInUsersName() {
        wait.forPresenceOfElements(25, By.tagName("ngb-modal-window"), "modal");
//        this.modalContainer.findElement(By.cssSelector("tr:nth-child(1) > td")).click();
        return this.driver.findElement(By.cssSelector("a:nth-child(2)")).getText();
    }

    void goToLoginPage() {
        this.loginButton.click();
        wait.forLoading(5);
    }
}