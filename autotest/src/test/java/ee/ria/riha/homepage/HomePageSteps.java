package ee.ria.riha.homepage;

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
    public void aUserNavigatesToHomePage(String country) {
        this.homePage.goToHomePage(country);
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
    public void userClicksOnLoginButton(String name) {
        Assert.assertTrue("Displayed logged in user's name is " + this.homePage.getLoggedInUsersName() + " instead of " + name, name.equals(this.homePage.getLoggedInUsersName()));
    }
}
