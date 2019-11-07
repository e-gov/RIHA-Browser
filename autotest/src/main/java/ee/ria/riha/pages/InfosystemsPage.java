package ee.ria.riha.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InfosystemsPage extends BasePage {
    @FindBy(id = "topics-input")
    private WebElement topicsInput;

    @FindBy(id = "info-systems-table")
    private WebElement infosystemsTable;

    public InfosystemsPage() {
        PageFactory.initElements(driver, this);
    }

    public List<String> getFoundInfosystemsTopics() throws InterruptedException {
        this.wait.forElementToBeDisplayed(10, infosystemsTable, "info-systems-table");
        Thread.sleep(2000);
        List<String> topics = new ArrayList<>();

        List<WebElement> tableRows = infosystemsTable.findElements(By.tagName("tr"));
        tableRows.forEach(row -> {
            if(row.findElements(By.className("topics")).size() > 0) {
                topics.add(row.findElement(By.className("topics")).findElements(By.tagName("button")).stream().map(WebElement::getText).collect(Collectors.joining(",")));
            }
        });

        return topics;
    }

    public String getTopicInputText() {
        return topicsInput.getAttribute("value");
    }
}
