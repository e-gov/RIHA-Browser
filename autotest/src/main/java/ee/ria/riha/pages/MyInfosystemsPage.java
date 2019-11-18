package ee.ria.riha.pages;

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

public class MyInfosystemsPage extends BasePage {
    @FindBy(xpath = "//main[@id='content']/app-producer-list/section/div[2]/a[4]")
    private WebElement detailedSearchLink;

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

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(id = "infosystems-table")
    private WebElement infosystemsTable;

    public MyInfosystemsPage() {
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
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, createNewLink, "createNewLink");
        createNewLink.click();
    }

    public void enterNameShortNameAndPurpose(String name, String shortName, String purpose) {
        nameInput.sendKeys(name);
        shortNameInput.sendKeys(shortName);
        purposeInput.sendKeys(purpose);
        submitButton.click();
    }

    public void waitForLoading() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, infosystemsTable, "infosystems-table");
        wait.sleep(TABLE_SORT_TIMEOUT);
    }
}
