package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.stream.Collectors;

import static ee.ria.riha.Timeouts.DISPLAY_ELEMENT_TIMEOUT;
import static ee.ria.riha.Timeouts.TABLE_SORT_TIMEOUT;
import static ee.ria.riha.context.ScenarioContext.*;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.RETURN;

public class InfosystemPage extends BasePage {

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/div[2]/p")
    private WebElement purposeP;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/h1")
    private WebElement nameDiv;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/h2")
    private WebElement shortNameDiv;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/div[3]/div")
    private WebElement topicsDiv;

    @FindBy(css = ".ng2-tags-container")
    private WebElement topicTagsContainer;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div[2]/div[5]/div/label/a")
    private WebElement homepageA;

    @FindBy(xpath = "//div[@id='uldkirjeldus']/app-producer-details-general/section/div/button")
    private WebElement editButton;

    @FindBy(xpath = "//div[@id='kontaktid']/app-producer-details-contacts/section/div/button")
    private WebElement editContactsButton;

    @FindBy(xpath = "//div[@id='dokumentatsioon']/app-producer-details-tech-docs/section/div/button")
    private WebElement editDocumentationButton;

    @FindBy(xpath = "//div[@id='andmed']/app-producer-details-objects/section/div/button")
    private WebElement editDataButton;

    @FindBy(xpath = "//div[@id='kontaktid']/app-producer-details-contacts/section/div[2]/table/tr/td[2]/span")
    private WebElement contactNameSpan;

    @FindBy(xpath = "//div[@id='kontaktid']/app-producer-details-contacts/section/div[2]/p")
    private WebElement contactsP;

    @FindBy(css = ".btn-success")
    private WebElement saveButton;

    @FindBy(id = "name-input")
    private WebElement nameInput;

    @FindBy(id = "short-name-input")
    private WebElement shortNameInput;

    @FindBy(id = "purpose-textarea")
    private WebElement purposeTextarea;

    @FindBy(id = "homepage")
    private WebElement homepageInput;

    @FindBy(css = ".ng2-tag-input__text-input")
    private WebElement topicsInput;

    @FindBy(css = "app-producer-details-tech-docs table")
    private WebElement techDocTable;

    @FindBy(xpath = "//div[@id='andmed']/app-producer-details-objects/section/div[2]/div/table")
    private WebElement dataObjectsTable;

    @FindBy(xpath = "//div[@id='andmed']/app-producer-details-objects/section/div[2]/div[2]/table")
    private WebElement dataUrlsTable;

    @FindBy(tagName = "ngb-modal-window")
    private WebElement modalContainer;

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

    public String getName() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, nameDiv, "nameDiv");
        return nameDiv.getText();
    }

    public String getShortName() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, shortNameDiv, "shortNameDiv");
        return shortNameDiv.getText();
    }

    public String getHomepage() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, homepageA, "homepageA");
        return homepageA.getText();
    }

    public String getContactName() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, contactNameSpan, "contactNameSpan");
        return contactNameSpan.getText();
    }

    public void clickOnTopic(String topic) {
        topicsDiv.findElements(By.tagName("button")).stream().filter(button -> button.getText().equalsIgnoreCase(topic)).findFirst().get().click();
    }

    public void clickEditButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editButton, "editButton");
        editButton.click();
    }

    public void clickEditContactsButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editContactsButton, "editContactsButton");
        editContactsButton.click();
    }

    public void clickSaveButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, saveButton, "saveButton");
        saveButton.click();
    }

    public void changeGeneralDescription(String suffix) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, nameInput, "nameInput");

        scenarioContext.saveToContext(NAME_KEY, nameInput.getAttribute("value"));
        this.nameInput.sendKeys(suffix);

        scenarioContext.saveToContext(SHORT_NAME_KEY, shortNameInput.getAttribute("value"));
        shortNameInput.sendKeys(suffix);

        scenarioContext.saveToContext(PURPOSE_KEY, purposeTextarea.getAttribute("value"));
        purposeTextarea.sendKeys(suffix);

        scenarioContext.saveToContext(HOMEPAGE_KEY, homepageInput.getAttribute("value"));
        homepageInput.sendKeys(suffix);
    }

    public void revertChangesInGeneralDescription() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, nameInput, "nameInput");

        nameInput.sendKeys(BACK_SPACE);
        shortNameInput.sendKeys(BACK_SPACE);
        purposeTextarea.sendKeys(BACK_SPACE);
        homepageInput.sendKeys(BACK_SPACE);
    }

    public void enterContactNameAndEmail(String name, String email) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        modalContainer.findElement(By.id("name")).sendKeys(name);
        modalContainer.findElement(By.id("email")).sendKeys(email);
        modalContainer.findElement(By.cssSelector(".col-12 > .btn")).click();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    public void deleteContact() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        modalContainer.findElement(By.cssSelector(".btn-danger")).click();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    public String getEmptyContactsPlaceholder() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, contactsP, "contactsP");
        return contactsP.getText();
    }

    public void enterNewTopic(String topic) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, topicsInput, "topicsInput");
        topicsInput.sendKeys(topic);
        topicsInput.sendKeys(RETURN);
    }

    public void removeTopic(String topic) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, topicTagsContainer, "topicTagsContainer");
        WebElement topicTag = topicTagsContainer.findElements(By.tagName("tag"))
                .stream()
                .filter(tag -> tag.findElement(By.tagName("div")).getAttribute("aria-label").equalsIgnoreCase(topic))
                .findFirst()
                .get();
        topicTag.findElement(By.tagName("delete-icon")).click();
    }

    public void clickEditDocumentationButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editDocumentationButton, "editDocumentationButton");
        editDocumentationButton.click();
    }

    public void clickOnAddNewUrlButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        modalContainer.findElement(By.cssSelector(".btn-secondary:nth-child(2)")).click();
    }

    public void clickEditDataButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editDataButton, "editDataButton");
        editDataButton.click();
    }

    public void enterNewTechnicalDocumentationLink(String url, String name) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        modalContainer.findElement(By.id("url")).sendKeys(url);
        modalContainer.findElement(By.id("linkName")).sendKeys(name);

        WebElement linkTypeElement = modalContainer.findElement(By.id("linkType"));
        linkTypeElement.click();
        new Select(linkTypeElement).selectByValue("DOC_TYPE_OTHER");

        modalContainer.findElement(By.cssSelector(".col-12 > .btn")).click();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    public String getTechDocUrls() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, techDocTable, "techDocTable");
        wait.sleep(TABLE_SORT_TIMEOUT);
        return techDocTable.findElements(By.cssSelector(".name")).stream()
                .map(td -> td.findElement(By.tagName("a")))
                .map(WebElement::getText)
                .collect(Collectors.joining(","));

    }

    public void removeLinkToTechnicalDocumentation(String name) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        WebElement techDocLinkDiv = modalContainer.findElement(By.className("expandable-blocks"))
                .findElements(By.className("expandable-block"))
                .stream()
                .filter(div -> div.findElement(By.tagName("a")).getText().equals(name))
                .findFirst().get();

        techDocLinkDiv.findElement(By.className("btn-danger")).click();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    public void addDataObjectFileNadUrlToInfosystem(String dataObject, String url, String urlName) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        //add data object
        modalContainer.findElement(By.id("object")).sendKeys(dataObject);
        modalContainer.findElement(By.cssSelector(".col-2 > .btn")).click();

        //add url
        modalContainer.findElement(By.id("url")).sendKeys(url);
        modalContainer.findElement(By.id("name")).sendKeys(urlName);
        modalContainer.findElement(By.cssSelector(".col-12 > .btn")).click();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    public String getDataObjects() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, dataObjectsTable, "dataObjectsTable");
        wait.sleep(TABLE_SORT_TIMEOUT);

        return dataObjectsTable.findElements(By.tagName("tr"))
                .stream()
                .map(tr -> tr.findElement(By.cssSelector("td:nth-child(2)")))
                .map(WebElement::getText)
                .collect(Collectors.joining(","));
    }

    public String getDataUrls() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, dataUrlsTable, "dataUrlsTable");
        wait.sleep(TABLE_SORT_TIMEOUT);

        return dataUrlsTable.findElements(By.tagName("tr"))
                .stream()
                .map(tr -> tr.findElement(By.cssSelector("td:nth-child(2) > a")))
                .map(WebElement::getText)
                .collect(Collectors.joining(","));
    }

    public void removeDataObjectAndUrl(String dataObject, String urlName) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        //remove data object
        WebElement dataObjectTr = modalContainer.findElement(By.cssSelector(".details-list-table:nth-child(1)"))
                .findElements(By.tagName("tr"))
                .stream()
                .filter(tr -> tr.findElement(By.cssSelector("td:nth-child(2)")).getText().equals(dataObject))
                .findFirst().get();

        dataObjectTr.findElement(By.className("btn-danger")).click();

        //remove url
        WebElement dataUrlTr = modalContainer.findElement(By.cssSelector(".dataTables_wrapper:nth-child(4) > .details-list-table"))
                .findElements(By.tagName("tr"))
                .stream()
                .filter(tr -> tr.findElement(By.cssSelector("td:nth-child(2) > a")).getText().equals(urlName))
                .findFirst().get();

        dataUrlTr.findElement(By.className("btn-danger")).click();

        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }
}