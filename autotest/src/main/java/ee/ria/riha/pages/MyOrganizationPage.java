package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;

public class MyOrganizationPage extends BasePage {


    @FindBy(id = "users-table")
    private WebElement employeesTable;

    @FindBy(css = "th:nth-child(1) .btn")
    private WebElement firstNameColumnSortHeader;

    @FindBy(css = "th:nth-child(2) .btn")
    private WebElement lastNameColumnSortHeader;

    @FindBy(css = "th:nth-child(3) .btn")
    private WebElement emailColumnSortHeader;

    @FindBy(css = "th:nth-child(4) .btn")
    private WebElement describerColumnSortHeader;

    @FindBy(css = "th:nth-child(5) .btn")
    private WebElement approverColumnSortHeader;


    public MyOrganizationPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public int getNumberOfEmployees() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, employeesTable, "employeesTable");
        List<WebElement> elements = driver.findElements(By.xpath("//tr"));
        return elements.size();
    }

    public void clickOnSortableHeaders() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, firstNameColumnSortHeader, "firstNameColumnSortHeader");
        firstNameColumnSortHeader.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, lastNameColumnSortHeader, "lastNameColumnSortHeader");
        lastNameColumnSortHeader.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, emailColumnSortHeader, "emailColumnSortHeader");
        emailColumnSortHeader.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, describerColumnSortHeader, "describerColumnSortHeader");
        describerColumnSortHeader.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, approverColumnSortHeader, "approverColumnSortHeader");
        approverColumnSortHeader.click();
    }
}
