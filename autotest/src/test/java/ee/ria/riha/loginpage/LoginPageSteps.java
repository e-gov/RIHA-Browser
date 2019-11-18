package ee.ria.riha.loginpage;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class LoginPageSteps {
    private LoginPage loginPage;

    public LoginPageSteps() {
        loginPage = Setup.pageFactory.getPage(LoginPage.class);
    }

    @When("LoginPage: user selects 'Mobiil-ID' tab")
    public void userSelectsMobiilIdTab() {
        loginPage.selectMobiilIdTab();
    }

    @And("LoginPage: user enters 'Isikukood' {string} and 'Telefoninumber' {string}")
    public void userEntersIsikukoodAndTelefoninumber(String personalCode, String phoneNumber) {
        loginPage.enterIsikukoodAndTelefoninumber(personalCode, phoneNumber);
    }
}
