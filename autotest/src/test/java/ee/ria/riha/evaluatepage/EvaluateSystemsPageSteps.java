package ee.ria.riha.evaluatepage;

import ee.ria.riha.driver.*;
import ee.ria.riha.pages.*;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

public class EvaluateSystemsPageSteps {
    private EvaluateSystemsPage evaluateSystemsPage;

    public EvaluateSystemsPageSteps() {
        evaluateSystemsPage = Setup.pageFactory.getPage(EvaluateSystemsPage.class);
    }

    @Then("EvaluateSystemsPage: date field for the last infosystem in list is not red")
    public void getLastInfosystemDateClass() {
        assertFalse("Infosystem date is red ", evaluateSystemsPage.getLastInfosystemDateClass().contains("text-danger"));
    }


    @When("EvaluateSystemsPage: user clicks on last infosystem")
    public void userClicksLastInfosystem() {
        evaluateSystemsPage.clickLastInfosystem();
    }

    @And("EvaluateSystemsPage: previously saved system is visible in required feedback table")
    public void isPreviouslySavedSystemVisibleInTheFeedbackTable() {
        assertTrue("previously saved system is not visible:", evaluateSystemsPage.isPreviouslySavedSystemVisibleInTheFeedbackTable());
    }
    @And("EvaluateSystemsPage: previously saved system is not visible in required feedback table")
    public void isPreviouslySavedSystemNotVisibleInTheFeedbackTable() {
        assertFalse("previously saved system is visible:", evaluateSystemsPage.isPreviouslySavedSystemVisibleInTheFeedbackTable());
    }

    @And("EvaluateSystemsPage: previously saved system feedback deadline date is not red")
    public void isPreviouslySavedSystemFeedbackDeadlineDateIsNotRed() {
        assertTrue("previously saved system feedback deadline date is red:", evaluateSystemsPage.isPreviouslySavedSystemFeedbackDeadlineDateIsNotRed());
    }

    @When("EvaluateSystemsPage: previously saved system shortName link is clicked")
    public void clickOnPreviouslySavedSystemLink(){
        evaluateSystemsPage.clickOnPreviouslySavedSystemLink();
    }
}
