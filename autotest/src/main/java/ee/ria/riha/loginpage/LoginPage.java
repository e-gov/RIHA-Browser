package ee.ria.riha.loginpage;

import ee.ria.riha.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {
    @FindBy(css = ".c-tab-login__nav-label > .icon-mobile-id")
    private WebElement mobiilIdTab;

    @FindBy(id = "mid-personal-code")
    private WebElement isikukoodInput;

    @FindBy(id = "mid-phone-number")
    private WebElement telefoninumberInput;

    @FindBy(css = "tr:nth-child(3) .c-btn")
    private WebElement jatkanButton;

    LoginPage() {
        PageFactory.initElements(driver, this);
    }

    void selectsMobiilIdTab() {
        this.mobiilIdTab.click();
    }

    void enterIsikukoodAndTelefoninumber(String personalCode, String phoneNumber) {
        this.isikukoodInput.sendKeys(personalCode);
        this.telefoninumberInput.sendKeys(phoneNumber);
        this.jatkanButton.click();
    }
}
