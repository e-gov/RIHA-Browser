package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.stream.Collectors;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;

public class InfosystemPage extends BasePage {

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/div[2]/p")
    private WebElement purposeP;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/div[3]/div")
    private WebElement topicsDiv;

    public InfosystemPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public String getAssociatedTopicsList() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, topicsDiv, "topicsDiv");
        return topicsDiv.findElements(By.tagName("button")).stream().map(WebElement::getText).collect(Collectors.joining(","));
    }

    public String getPurposeText() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, purposeP, "purposeP");
        return purposeP.getText();
    }

    public void clickOnTopic(String topic) {
        topicsDiv.findElements(By.tagName("button")).stream().filter(button -> button.getText().equalsIgnoreCase(topic)).findFirst().get().click();
    }
}
