package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;
import static ee.ria.riha.Timeouts.TABLE_SORT_TIMEOUT;

public class InfosystemsPage extends BasePage {
    @FindBy(xpath = "//main[@id='content']/app-browser-list/section/div[2]/a")
    private WebElement detailedSearchLink;

    @FindBy(id = "owner-name-input")
    private WebElement ownerInput;

    @FindBy(id = "name-input")
    private WebElement nameInput;

    @FindBy(id = "topics-input")
    private WebElement topicsInput;

    @FindBy(id = "info-systems-table")
    private WebElement infosystemsTable;

    public InfosystemsPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public List<String> getFoundInfosystemsTopics() {
        waitForLoading();

        List<String> topics = new ArrayList<>();

        List<WebElement> tableRows = infosystemsTable.findElements(By.tagName("tr"));
        tableRows.forEach(row -> {
            if (row.findElements(By.className("topics")).size() > 0) {
                topics.add(row.findElement(By.className("topics")).findElements(By.tagName("button")).stream().map(WebElement::getText).collect(Collectors.joining(",")));
            }
        });

        return topics;
    }

    public List<String> getFoundInfosystemsShortName() {
        waitForLoading();

        List<String> sortNames = new ArrayList<>();

        List<WebElement> tableRows = infosystemsTable.findElements(By.tagName("tr"));
        tableRows.forEach(row -> {
            sortNames.add(row.findElement(By.xpath("//td[2]")).findElement(By.tagName("a")).getText());
        });

        return sortNames;
    }

    public void openDetailedSearchForm() {
        waitForLoading();
        detailedSearchLink.click();
    }

    public String getTopicInputText() {
        waitForLoading();
        return topicsInput.getAttribute("value");
    }

    public String getOwnerInputText() {
        return ownerInput.getAttribute("value");
    }

    public String getNameInputText() {
        return nameInput.getAttribute("value");
    }

    public void searchByOwnerNameAndTopic(String owner, String name, String topic) {
        ownerInput.sendKeys(owner);
        nameInput.sendKeys(name);
        topicsInput.sendKeys(topic);
        topicsInput.sendKeys(Keys.RETURN);
        waitForLoading();
    }

    private void waitForLoading() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, infosystemsTable, "info-systems-table");
        wait.sleep(TABLE_SORT_TIMEOUT);
    }
}
