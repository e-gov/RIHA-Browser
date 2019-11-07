package ee.ria.riha.loginpage;

import ee.ria.riha.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class LoginPageSteps {
    private LoginPage loginPage;

    public LoginPageSteps() {
        this.loginPage = new LoginPage();
    }

    @When("^user selects 'Mobiil-ID' tab$")
    public void userSelectsMobiilIdTab() {
        this.loginPage.selectMobiilIdTab();
    }

    @And("^user enters 'isikukood' \"([^\"]*)\" and 'Telefoninumber' \"([^\"]*)\"$")
    public void userEntersIsikukoodAndTelefoninumber(String personalCode, String phoneNumber) {
        this.loginPage.enterIsikukoodAndTelefoninumber(personalCode, phoneNumber);
    }
}
