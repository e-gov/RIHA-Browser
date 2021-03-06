package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;

public class MyDiscussionsPage extends BasePage {

    public static final String DISCUSSION_SYSTEM_SHORT_NAME = "DISCUSSION_SYSTEM_SHORT_NAME";
    public static final String DISCUSSION_TITLE_NAME = "DISCUSSION_TITLE_NAME";

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

    public void clickOnFirstDiscussionShortNameAndRememberIt() {
        WebElement shortNameLink = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(2) > a"));
        scenarioContext.saveToContext(DISCUSSION_SYSTEM_SHORT_NAME, shortNameLink.getText());
        shortNameLink.click();
    }

    public boolean isUserRedirectedToSystemPageWithRememberedShortName() {
        wait.sleep(2000);
        return driver.findElement(By.cssSelector("h2"))
                .getText().equalsIgnoreCase(scenarioContext.getFromContext(DISCUSSION_SYSTEM_SHORT_NAME));
    }

    public void clickOnFirstDiscussionTitleLinkAndRememberIt() {
        WebElement discussionTitleLink = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(3) > a"));
        scenarioContext.saveToContext(DISCUSSION_TITLE_NAME, discussionTitleLink.getText());
        discussionTitleLink.click();
    }

    public boolean isUserRedirectedToSystemPageAndPopupIsOpened() {
        wait.sleep(2000);
        return driver.findElement(By.cssSelector(".text-gray-dark:nth-child(1)"))
                .getText().equalsIgnoreCase(scenarioContext.getFromContext(DISCUSSION_TITLE_NAME));
    }
}
