package ee.ria.riha.pages;

import ee.ria.riha.context.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.*;

import java.util.*;
import java.util.stream.*;

import static ee.ria.riha.Timeouts.*;
import static ee.ria.riha.context.ScenarioContext.*;

public class MyInfosystemsPage extends BasePage {
    @FindBy(xpath = "//main[@id='content']/app-producer-list/section/div[2]/a[4]")
    private WebElement detailedSearchLink;

    @FindBy(id = "search-text-input")
    private WebElement searchTextInput;

    @FindBy(id = "topics-input")
    private WebElement topicsInput;

    @FindBy(id = "name-input")
    private WebElement nameInput;

    @FindBy(id = "short-name-input")
    private WebElement shortNameInput;

    @FindBy(id = "purpose-textarea")
    private WebElement purposeInput;

    @FindBy(css = ".table-responsive > .btn")
    private WebElement createNewLink;

    @FindBy(linkText = "Minu arutelud")
    private WebElement myDiscussionsLink;

    @FindBy(linkText = "Minu organisatsioon")
    private WebElement myOrganization;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(id = "infosystems-table")
    private WebElement infosystemsTable;

    @FindBy(xpath = "//table[@id='infosystems-table']/thead/th[6]/app-sort-button/button")
    private WebElement sortByLastModifiedButton;

    public MyInfosystemsPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public void openDetailedSearchForm() {
        detailedSearchLink.click();
    }

    public void searchByTopic(String topic) {
        topicsInput.sendKeys(topic);
        topicsInput.sendKeys(Keys.RETURN);
    }

    public List<String> getFoundInfosystemsTopics() {
        waitForLoading();

        List<String> topics = new ArrayList<>();

        List<WebElement> tableRows = infosystemsTable.findElements(By.tagName("tr"));
        tableRows.forEach(row -> {
            topics.add(row.findElement(By.className("topics")).findElements(By.tagName("button")).stream().map(WebElement::getText).collect(Collectors.joining(",")));
        });

        return topics;
    }

    public void goToInfosystemPage(String infosystem) {
        driver.findElement(By.linkText(infosystem)).click();
    }

    public void goToCreateNewInfosystemPage() {
        wait.sleep(1000);
        scrollToElement(createNewLink);
        wait.sleep(1000);
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, createNewLink, "createNewLink");
        createNewLink.click();
    }

    public void goToMyDiscussionsInfosystemPage() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, myDiscussionsLink, "myDiscussionsLink");
        myDiscussionsLink.click();
    }

    public void goToMyOrganizationInfosystemPage() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, myOrganization, "myOrganizationLink");
        myOrganization.click();
    }

    public void enterNameShortNameAndPurpose(String namePrefix, String shortNamePrefix, String purpose) {
        String lastCreatedInfosystemNumber = scenarioContext.getFromContext(LAST_INFOSYSTEM_NUMBER);

        int i = Integer.parseInt(lastCreatedInfosystemNumber);
        i++;

        String systemName = namePrefix + " " + i;
        nameInput.sendKeys(systemName);
        scenarioContext.saveToContext(CREATED_SYSTEM_NAME, systemName);

        String shortName = shortNamePrefix + "-" + i;
        shortNameInput.sendKeys(shortName);
        scenarioContext.saveToContext(CREATED_SYSTEM_SHORT_NAME, shortName);

        purposeInput.sendKeys(purpose);
        submitButton.click();
    }

    public void enterSearchText(String text) {
        waitForLoading();
        scenarioContext.saveToContext(SEARCH_TEXT_KEY, text);
        searchTextInput.sendKeys(text);
        searchTextInput.sendKeys(Keys.RETURN);
        waitForLoading();
    }

    public void sortByLastModifiedDesc() {
        sortByLastModifiedButton.click(); //asc
        waitForLoading();
        sortByLastModifiedButton.click(); //desc
        waitForLoading();
    }

    public void saveFirstFoundInfosystemShortNameToScenarioContext() {
        WebElement firstRow = infosystemsTable.findElement(By.xpath("//tr[1]"));
        String shortName = firstRow.findElement(By.xpath("//td[1]")).findElement(By.tagName("a")).getText();
        String lastInfoSystemNumber;
        if (shortName.equalsIgnoreCase(scenarioContext.getFromContext(SEARCH_TEXT_KEY))) {
            lastInfoSystemNumber = "1";
        } else {
            lastInfoSystemNumber = shortName.substring(scenarioContext.getFromContext(SEARCH_TEXT_KEY).length() + 1);
        }
        scenarioContext.saveToContext(LAST_INFOSYSTEM_NUMBER, lastInfoSystemNumber);
    }

    public void selectFirstFoundInfosystem() {
        WebElement firstRow = infosystemsTable.findElement(By.xpath("//tr[1]"));
        firstRow.findElement(By.xpath("//td[1]")).findElement(By.tagName("a")).click();
    }

    private void waitForLoading() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, infosystemsTable, "infosystems-table");
        wait.sleep(TABLE_SORT_TIMEOUT);
    }

}
