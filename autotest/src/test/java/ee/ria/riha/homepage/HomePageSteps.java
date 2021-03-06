package ee.ria.riha.homepage;

import ee.ria.riha.driver.*;
import ee.ria.riha.pages.*;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;

import static org.junit.Assert.*;

public class HomePageSteps {
    private HomePage homePage;

    public HomePageSteps() {
        homePage = Setup.pageFactory.getPage(HomePage.class);
    }

    @Given("HomePage: user navigates to page {string}")
    public void userNavigatesToPage(String url) {
        homePage.goToPage(url);
    }

    @And("HomePage: user cancels organization select dialog")
    public void userCancelsOrgSelectDialog() {
        homePage.getModalContainer().sendKeys(Keys.ESCAPE);
    }

    @Given("HomePage: user selects organization {string}")
    public void userSelectsOrganization(String organization) {
        homePage.selectOrganization(organization);
    }

    @And("HomePage: user opens 'infosystems' page")
    public void userOpensInfosystemsPage() {
        homePage.goToInfosystemsPage();
    }

    @And("HomePage: user opens 'my organization infosystems' page")
    public void userOpensMyInfosystemsPage() {
        homePage.goToMyInfosystemsPage();
    }

    @And("HomePage: user clicks on login button")
    public void userClicksOnLoginButton() {
        homePage.goToLoginPage();
    }

    @Then("HomePage: logo is displayed")
    public void googleLogoIsDisplayed() {
        homePage.checkLogoDisplay();
    }

    @Then("HomePage: page title is {string}")
    public void pageTitleIs(String title) {
        assertTrue("Displayed title is " + homePage.getTitle() + " instead of " + title, title.equals(homePage.getTitle()));
    }

    @Then("HomePage: home page is opened showing logged in user's name {string}")
    public void homePageIsOpenedShowingLoggedInUsersName(String name) {
        homePage.selectSecondOrganization();
        assertTrue("Displayed logged in user's name is " + homePage.getLoggedInUsersName() + " instead of " + name, homePage.getLoggedInUsersName().contains(name));
    }

    @Then("HomePage: organization name {string} is shown next to user's name")
    public void organizationNameIsShownNextToUsersName(String organizationName) {
        assertTrue("Organization name " + organizationName + " is not shown", homePage.getLoggedInUsersName().contains(organizationName));
    }

    @And("HomePage: user returns to the previous page")
    public void goOnePageBack() {
        homePage.goHistoryMinusOne();
    }

    @Given("HomePage: user clicks on the search bar and enters {string}")
    public void userClicksOnSearchBar(String word) {
        this.homePage.inputSearchTerm(word);
    }

    @And("HomePage: user clicks on evaluate button")
    public void userClicksEvaluate() {
        homePage.clickEvaluate();
    }

    @And("HomePage: user opens Select Organization dialog")
    public void openSelectOrganizationDialog() {
        homePage.openSelectOrganizationDialog();
    }

    @Then("HomePage: option {string} is visible in left menu")
    public void checkIfMenuOptionInLeftMenuIsVisible(String menuOptionLabel) {
        assertTrue(homePage.isLeftMenuOptionVisible(menuOptionLabel));
    }

    @And("HomePage: table with headers {string} is visible")
    public void checkIfTableWithHeadersIsVisible(String commaSeparatedHeaderNames) {
        assertTrue(homePage.checkIfTableWithHeadersIsVisible(commaSeparatedHeaderNames));
    }

    @Given("HomePage: user remembers systemName {} and shortName: {}")
    public void rememberSystemNameAndShortName(String systemName, String shortName) {
        homePage.rememberSystemNameAndShortName(systemName, shortName);
    }

}
