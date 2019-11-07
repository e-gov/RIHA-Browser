package ee.ria.riha.pages;

import ee.ria.riha.pages.BasePage;
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

    public LoginPage() {
        PageFactory.initElements(driver, this);
    }

    public void selectMobiilIdTab() {
        this.mobiilIdTab.click();
    }

    public void enterIsikukoodAndTelefoninumber(String personalCode, String phoneNumber) {
        this.isikukoodInput.sendKeys(personalCode);
        this.telefoninumberInput.sendKeys(phoneNumber);
        this.jatkanButton.click();
    }
}
