package ee.ria.riha.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.detailedSearchLink.click();
    }

    public void searchByTopic(String topic) throws Exception {
        this.topicsInput.sendKeys(topic);
        this.topicsInput.sendKeys(Keys.RETURN);
        this.wait.forElementToBeDisplayed(10, infosystemsTable, "infosystems-table");
        Thread.sleep(2000);
    }

    public List<String> getFoundInfosystemsTopics() {
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
        this.wait.forElementToBeDisplayed(15, createNewLink, "createNewLink");
        this.createNewLink.click();
    }

    public void enterNameSHortNameAndPurpose(String name, String shortName, String purpose) {
        this.nameInput.sendKeys(name);
        this.shortNameInput.sendKeys(shortName);
        this.purposeInput.sendKeys(purpose);
        this.submitButton.click();
    }
}
