package ee.ria.riha;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.HomePage;
import ee.ria.riha.pages.LoginPage;
import io.cucumber.java.en.Given;

public class BackgroundSteps {
    private HomePage homePage;
    private LoginPage loginPage;

    public BackgroundSteps() {
        loginPage = Setup.pageFactory.getPage(LoginPage.class);
        homePage = Setup.pageFactory.getPage(HomePage.class);
    }

    @Given("User is logged in to {string} as {string} using tel. number {string}")
    public void userIsLoggedInAsTestUser(String url, String personalCode, String phoneNumber) {
        homePage.goToPage(url);
        homePage.goToLoginPage();
        loginPage.selectMobiilIdTab();
        loginPage.enterIsikukoodAndTelefoninumber(personalCode, phoneNumber);
        homePage.selectSecondOrganization();
    }
}
