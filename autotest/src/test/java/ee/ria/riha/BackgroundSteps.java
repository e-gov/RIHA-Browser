package ee.ria.riha;

import ee.ria.riha.pages.HomePage;
import ee.ria.riha.pages.LoginPage;
import io.cucumber.java.en.Given;

public class BackgroundSteps {
    private HomePage homePage;
    private LoginPage loginPage;

    public BackgroundSteps() {
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();
    }

    @Given("^User is logged in to \"([^\"]*)\" as \"([^\"]*)\" using tel. number \"([^\"]*)\"$")
    public void userIsLoggedInAsTestUser(String url, String personalCode, String phoneNumber) {
        this.homePage.goToHomePage(url);
        this.homePage.goToLoginPage();
        this.loginPage.selectMobiilIdTab();
        this.loginPage.enterIsikukoodAndTelefoninumber(personalCode, phoneNumber);
        this.homePage.selectSecondOrganization();
    }
}
