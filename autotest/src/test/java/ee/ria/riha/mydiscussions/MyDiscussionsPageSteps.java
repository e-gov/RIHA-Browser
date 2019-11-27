package ee.ria.riha.mydiscussions;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.MyDiscussionsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertTrue;

public class MyDiscussionsPageSteps {

    private final MyDiscussionsPage myDiscussionsPage;

    public MyDiscussionsPageSteps() {
        myDiscussionsPage = Setup.pageFactory.getPage(MyDiscussionsPage.class);
    }

    @Then("MyDiscussionsPage: table with discussions is displayed")
    public void myDiscussionsTableIsDisplayed() {
        assertTrue(myDiscussionsPage.getNumberOfDiscussion() > 0);
    }

    @And("MyDiscussionsPage: table with discussions is sortable")
    public void clickOnSortableHeaders() {
        myDiscussionsPage.clickOnSortableHeaders();
    }
}
