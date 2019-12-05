package ee.ria.riha.pages;

import ee.ria.riha.context.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.*;

import static ee.ria.riha.Timeouts.*;

public class EvaluateSystemsPage extends BasePage{

    @FindBy(xpath = "//table[@id='infosystems-table']/tbody/tr[last()]/td[last()]")
    private WebElement lastInfosystemDate;

    @FindBy(xpath = "//table[@id='infosystems-table']/tbody/tr[last()]/td/a")
    private WebElement lastInfosystem;

    public EvaluateSystemsPage(ScenarioContext scenarioContext) {
        super(scenarioContext);
        PageFactory.initElements(driver, this);
    }

    public void clickLastInfosystem() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, lastInfosystem, "lastInfosystem");
        lastInfosystem.click();
    }

    public String getLastInfosystemDateClass() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, lastInfosystemDate, "lastInfosystemDate");
       return lastInfosystemDate.getAttribute("class");
    }

    public  boolean isPreviouslySavedSystemVisibleInTheFeedbackTable() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, lastInfosystem, "lastInfosystem");
        String savedShortName = scenarioContext.getFromContext(ScenarioContext.CREATED_SYSTEM_SHORT_NAME);
        return savedShortName.trim().equalsIgnoreCase(lastInfosystem.getText().trim());
    }

    public boolean isPreviouslySavedSystemFeedbackDeadlineDateIsNotRed() {
        return !getLastInfosystemDateClass().contains("text-danger");
    }

    public void clickOnPreviouslySavedSystemLink() {
        if (!isPreviouslySavedSystemVisibleInTheFeedbackTable()) {
            return;
        }

        lastInfosystem.click();
    }
}
