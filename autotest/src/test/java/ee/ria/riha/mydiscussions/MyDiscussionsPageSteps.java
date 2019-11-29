package ee.ria.riha.mydiscussions;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.MyDiscussionsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
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

    @Given("MyDiscussionsPage: user clicks on discussion shortName link and remembers the shortName")
    public void clickOnFirstDiscussionShortNameAndRememberIt() {
        myDiscussionsPage.clickOnFirstDiscussionShortNameAndRememberIt();
    }

    @Then("MyDiscussionsPage: user is located on the selected infosystem page")
    public void checkTheInfoSystemPage() {
        assertTrue(myDiscussionsPage.isUserRedirectedToSystemPageWithRememberedShortName());
    }

    @Given("MyDiscussionsPage: user clicks on discussion discussion title link and remembers the title")
    public void clickOnFirstDiscussionTitleLinkAndRememberIt() {
        myDiscussionsPage.clickOnFirstDiscussionTitleLinkAndRememberIt();
    }

    @Then("MyDiscussionsPage: user navigates to the selected discussion popup")
    public void checkTheSystemPageAndPopup() {
        assertTrue(myDiscussionsPage.isUserRedirectedToSystemPageAndPopupIsOpened());
    }
}
