package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;

public class MyDiscussionsPage extends BasePage {


    @FindBy(id = "infosystems-table")
    private WebElement myDiscussionsTable;

    @FindBy(css = "th:nth-child(1) .btn")
    private WebElement nameFilter;

    @FindBy(css = "th:nth-child(2) .btn")
    private WebElement shortNameFilter;

    @FindBy(css = "th:nth-child(3) .btn")
    private WebElement titleFilter;

    @FindBy(css = "th:nth-child(4) .btn")
    private WebElement lastCommentDateFilter;

    public MyDiscussionsPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public int getNumberOfDiscussion() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, myDiscussionsTable, "myDiscussionsTable");
        List<WebElement> elements = driver.findElements(By.xpath("//tr"));
        return elements.size();
    }

    public void clickOnSortableHeaders() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, nameFilter, "myDiscussionsTable");
        nameFilter.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, shortNameFilter, "myDiscussionsTable");
        shortNameFilter.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, titleFilter, "myDiscussionsTable");
        titleFilter.click();
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, lastCommentDateFilter, "myDiscussionsTable");
        lastCommentDateFilter.click();
    }
}
