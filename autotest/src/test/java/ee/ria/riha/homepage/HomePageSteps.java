package ee.ria.riha.homepage;

import ee.ria.riha.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class HomePageSteps {
    private HomePage homePage;

    public HomePageSteps() {
        this.homePage = new HomePage();
    }

    @Given("^A user navigates to HomePage \"([^\"]*)\"$")
    public void aUserNavigatesToHomePage(String url) {
        this.homePage.goToHomePage(url);
    }

    @Given("^User selects organization \"([^\"]*)\"$")
    public void userSelectsOrganization(String organization) {
        this.homePage.selectOrganization(organization);
    }

    @Given("^User opens 'my organization infosystems page'$")
    public void userOpensMyInfosystemsPage() {
        this.homePage.goToMyInfosystemsPage();
    }

    @And("^user clicks on login button$")
    public void userClicksOnLoginButton() {
        this.homePage.goToLoginPage();
    }

    @Then("^logo is displayed$")
    public void googleLogoIsDisplayed() {
        this.homePage.checkLogoDisplay();
    }

    @Then("^page title is \"([^\"]*)\"$")
    public void pageTitleIs(String title) {
        Assert.assertTrue("Displayed title is " + this.homePage.getTitle() + " instead of " + title, title.equals(this.homePage.getTitle()));
    }

    @Then("^home page is opened showing logged in user's name \"([^\"]*)\"$")
    public void homePageIsOpenedShowingLoggedInUsersName(String name) {
        this.homePage.selectSecondOrganization();
        Assert.assertTrue("Displayed logged in user's name is " + this.homePage.getLoggedInUsersName() + " instead of " + name, this.homePage.getLoggedInUsersName().contains(name));
    }

    @Then("^Organization name \"([^\"]*)\" is shown next to user's name$")
    public void organizationNameIsShownNextToUsersName(String organizationName) {
        Assert.assertTrue("Organization name " + organizationName + " is not shown", this.homePage.getLoggedInUsersName().contains(organizationName));
    }
}
