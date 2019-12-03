package ee.ria.riha;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.driver.Wait;
import ee.ria.riha.pages.HomePage;
import ee.ria.riha.pages.InfosystemPage;
import ee.ria.riha.pages.LoginPage;
import ee.ria.riha.pages.MyInfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class BackgroundSteps {

    public static final String FILE_UPLOAD_NAME = "test.txt";
    
    private final Wait wait;
    private HomePage homePage;
    private LoginPage loginPage;
    private MyInfosystemsPage myInfosystemsPage;
    private final InfosystemPage infosystemPage;

    public BackgroundSteps() {
        loginPage = Setup.pageFactory.getPage(LoginPage.class);
        homePage = Setup.pageFactory.getPage(HomePage.class);
        myInfosystemsPage = Setup.pageFactory.getPage(MyInfosystemsPage.class);
        infosystemPage = Setup.pageFactory.getPage(InfosystemPage.class);
        wait = new Wait(Setup.driver);
    }

    @Given("User is logged in to {string} as {string} using tel. number {string}")
    public void userIsLoggedInAsTestUser(String url, String personalCode, String phoneNumber) {
        homePage.goToPage(url);
        homePage.goToLoginPage();
        loginPage.selectMobiilIdTab();
        loginPage.enterIsikukoodAndTelefoninumber(personalCode, phoneNumber);
        homePage.selectSecondOrganization();
    }

    @Given("BackgroundSteps: user creates system with name {string}, shortName {string}, purpose {string}," +
            " dataObject {string}, url {string}, urlName {string}," +
            " technicalDocumentationUrl {string}, technicalDocumentationLink {string}," +
            " contactName {string}, email {string}," +
            " legalUrl {string}, legalName {string}")
    public void userCreatesAnInfosystem(String namePrefix, String shortNamePrefix, String purpose, String dataObject,
                                        String url, String urlName, String technicalDocumentationUrl, String technicalDocumentationLink, String contactName, String email, String legalUrl, String legalName) {
        myInfosystemsPage.goToCreateNewInfosystemPage();
        myInfosystemsPage.enterNameShortNameAndPurpose(namePrefix, shortNamePrefix, purpose);

        infosystemPage.clickEditButton();
        infosystemPage.clickSelectSystemStatus("asutamisel");
        infosystemPage.clickSelectDevelopmentStatus(true);
        infosystemPage.clickSaveButton();
        infosystemPage.clickEditDataButton();

        infosystemPage.addDataObjectFileAndUrlToInfosystem(dataObject, FILE_UPLOAD_NAME,  url, urlName);
        testSleeps(2);

        infosystemPage.clickEditDocumentationButton();
        infosystemPage.clickOnAddNewDocumentationUrlButton();
        infosystemPage.enterNewTechnicalDocumentationLink(technicalDocumentationUrl, technicalDocumentationLink);
        testSleeps(2);

        infosystemPage.clickEditContactsButton();
        infosystemPage.enterContactNameAndEmail(contactName, email);
        testSleeps(5);

        infosystemPage.clickEditLegalActsButton();
        infosystemPage.enterNewLegalActInfo(legalUrl, legalName);
        testSleeps(2);
    }

    @And("Test sleeps for {int} seconds")
    public void testSleeps(int seconds) {
        homePage.getWait().sleep(seconds * 1000);
    }
}
