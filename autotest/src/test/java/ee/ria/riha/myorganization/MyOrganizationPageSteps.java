package ee.ria.riha.myorganization;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.MyOrganizationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertTrue;

public class MyOrganizationPageSteps {

    private final MyOrganizationPage myOrganizationPage;

    public MyOrganizationPageSteps() {
        myOrganizationPage = Setup.pageFactory.getPage(MyOrganizationPage.class);
    }

    @Then("MyOrganizationPage: table with employees is displayed")
    public void checkEmployeesTable() {
        assertTrue("number of employees must be greater than 0", myOrganizationPage.getNumberOfEmployees() > 0);
    }


    @And("MyOrganizationPage: table with discussions is sortable")
    public void clickOnSortableHeaders() {
        myOrganizationPage.clickOnSortableHeaders();
    }


}
