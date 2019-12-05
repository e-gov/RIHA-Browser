package ee.ria.riha.pages;

import ee.ria.riha.context.*;
import ee.ria.riha.util.*;
import org.apache.commons.lang3.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static ee.ria.riha.Timeouts.*;
import static ee.ria.riha.context.ScenarioContext.*;
import static org.openqa.selenium.Keys.*;

public class InfosystemPage extends BasePage {

    private ArrayList<String> tabs;
    public static final String ISSUE_TEXT = "ISSUE_TEXT";
    public static final String ISSUE_TITLE = "ISSUE_TITLE";

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

    @FindBy(xpath = "//div[@id='oigusaktid']/app-producer-details-legislations/section/div/button")
    private WebElement editLegalActsButton;

    @FindBy(xpath = "//div[@id='kontaktid']/app-producer-details-contacts/section/div/button")
    private WebElement editContactsButton;

    @FindBy(xpath = "//div[@id='dokumentatsioon']/app-producer-details-tech-docs/section/div/button")
    private WebElement editDocumentationButton;

    @FindBy(xpath = "//div[@id='dokumentatsioon']/app-producer-details-tech-docs/section/div[2]")
    private WebElement documentationSection;

    @FindBy(xpath = "//div[@id='tagasiside']/app-producer-details-issues/section/div[2]/button")
    private WebElement feedbackRequestButton;

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

    @FindBy(xpath = "//div[@id='ngb-tab-0-panel']/div/div/table")
    private WebElement openIssuesTable;

    @FindBy(tagName = "ngb-modal-window")
    private WebElement modalContainer;

    @FindBy(xpath = "//div[@id='seosed']/app-producer-details-relations/section/div/button")
    private WebElement AssociationsEditButton;

    @FindBy(xpath = "//a[contains(text(),'Seosed süsteemidega')]")
    private WebElement navigationToAssociations;

    @FindBy(xpath = "//input[@id='infoSystem']")
    private WebElement modalAssociationsShortName;

    @FindBy(css = "ngb-highlight")
    private WebElement modalAssociationsAutoOption;

    @FindBy(xpath = "//select[@id='type']")
    private WebElement associationTypeDropDown;

    @FindBy(xpath = "//form/div[3]/div/button")
    private WebElement saveNewAssociation;

    @FindBy(xpath = "//a[contains(@href, '/Infosüsteemid/Vaata/ummik.test')]")
    private WebElement newAssociatedInfosystemInModal;

    @FindBy(xpath = "//a[contains(text(),'Riigi infosüsteemi haldussüsteem (riha-test)')]")
    private WebElement newAssociatedInfosystemInDetail;

    @FindBy(xpath = "//tr[4]/td[3]/button/i")
    private WebElement deleteAssociationButton;

    @FindBy(xpath = "//header[@id='header']/div[2]/app-riha-navbar/div/div/div[2]/div/button/span")
    private WebElement logOutButton;

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

    public void clickEditLegalActsButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editLegalActsButton, "editButton");
        editLegalActsButton.click();
    }

    public void clickEditContactsButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, editContactsButton, "editContactsButton");
        editContactsButton.click();
    }

    public void clickSelectSystemStatus(String status) {
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.linkText(status), "status select links");
        driver.findElement(By.linkText(status)).click();
    }

    public void clickSelectDevelopmentStatus(boolean isActiveDevelopment) {
        String linkText = isActiveDevelopment ? "Jah" : "Ei";
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.linkText(linkText), "development status links");
        driver.findElement(By.linkText(linkText)).click();
    }

    public void clickRequestFeedbackButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, feedbackRequestButton, "feedback request button");
        feedbackRequestButton.click();
        wait.sleep(2000);
    }

    public void giveFeedback(String title, String comment) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.id("issueTitle"), "feedback comment");
        modalContainer.findElement(By.id("issueTitle")).sendKeys(title);

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.id("issueText"), "feedback comment");
        modalContainer.findElement(By.id("issueText")).sendKeys(comment);
    }

    public String getOpenIssues() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, openIssuesTable, "openIssuesTable");
        wait.sleep(TABLE_SORT_TIMEOUT);

        return openIssuesTable.findElements(By.tagName("tr"))
                .stream()
                .map(tr -> tr.findElement(By.cssSelector("td:nth-child(2)")))
                .map(td -> td.findElement(By.tagName("a")))
                .map(WebElement::getText)
                .collect(Collectors.joining(","));

    }

    public void userClicksOnUploadNewFileButton() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");
        modalContainer.findElement(By.cssSelector(".ml-2:nth-child(1)")).click();
    }

    public boolean isUploadedDateDisplayedOnTheLastUploadedDocument() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, documentationSection, "documentationSection");

        //sort by the end date desc
        driver.findElement(By.cssSelector("app-producer-details-tech-docs:nth-child(1) th:nth-child(3) .btn:nth-child(1)")).click();
        wait.sleep(1000);
        driver.findElement(By.cssSelector("app-producer-details-tech-docs:nth-child(1) th:nth-child(3) .btn:nth-child(1)")).click();
        wait.sleep(1000);

        List<WebElement> tableRows = documentationSection.findElements(By.tagName("tr"));
        //drop header
        tableRows.remove(0);
        //search for first date
        String formattedDate = tableRows.stream()
                .filter(row -> StringUtils.isNotBlank(((RemoteWebElement) row).findElementByCssSelector (".last-modified").getText()))
                .findFirst()
                .map(row -> ((RemoteWebElement) row).findElementByCssSelector (".last-modified").getText())
                .orElse(null);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(formattedDate);
            return Math.abs(date.getTime() - System.currentTimeMillis()) < 60000L;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum FeedbackType {
        ASUTAMISEKS(".custom-control:nth-child(1) > .custom-control-label"),
        KASUTUSELE_VOTMISEKS(".custom-control:nth-child(2) > .custom-control-label"),
        ANDMEKOOSSEISU_MUUTMISEKS(".custom-control:nth-child(3) > .custom-control-label"),
        LOPETAMISEKS(".custom-control:nth-child(4) > .custom-control-label");

        private String cssRule;

        FeedbackType(String cssRule) {
            this.cssRule = cssRule;
        }

        public String getCssRule() {
            return cssRule;
        }
    }

    public void requestFeedback(FeedbackType feedbackType, String comment) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");
        //request feedback select box
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.cssSelector(".switch"), "control switch");
        modalContainer.findElement(By.cssSelector(".switch")).click();

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.cssSelector(feedbackType.cssRule), feedbackType.name());
        modalContainer.findElement(By.cssSelector(feedbackType.cssRule)).click();

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.id("requestText"), "feedback comment");
        modalContainer.findElement(By.id("requestText")).sendKeys(comment);
    }

    public void userAddsFeedbackWithTypeAndComment(String type, String comment) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");
        modalContainer.findElement(By.name("commentText")).sendKeys(comment);

        new Select(modalContainer.findElement(By.name("decisionType"))).selectByVisibleText(type);

        modalContainer.findElement(By.cssSelector(".mx-2 > .btn")).click();
        wait.sleep(10000);
        modalContainer.findElement(By.cssSelector(".btn-secondary")).click();
    }

    public void createIssue(String title, String comment) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.id("issueTitle"), "issue title");
        modalContainer.findElement(By.id("issueTitle")).sendKeys(title);
        scenarioContext.saveToContext(ISSUE_TITLE, title);

        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.id("issueText"), "feedback comment");
        modalContainer.findElement(By.id("issueText")).sendKeys(comment);
        scenarioContext.saveToContext(ISSUE_TEXT, comment);

    }

    public boolean isIssueSaved() {
        String issueTitle = scenarioContext.getFromContext(ISSUE_TITLE);
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.linkText(issueTitle), "linkTitle");
        driver.findElement(By.linkText(issueTitle)).click();

        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        By titleCssSelector = By.cssSelector(".text-gray-dark:nth-child(1)");
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, titleCssSelector, "issue title");

        By issueTextSelector = By.cssSelector(".user-multiline-text:nth-child(1)");
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, issueTextSelector, "feedback comment");
        return modalContainer.findElement(titleCssSelector).getText().equals(issueTitle) &&
                modalContainer.findElement(issueTextSelector).getText().equals(scenarioContext.getFromContext(ISSUE_TEXT));
    }

    public boolean isFeedbackRequestTitleEditable() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");
        String readOnlyAttribute = modalContainer.findElement(By.id("requestTitle")).getAttribute("readonly");

        return readOnlyAttribute == null || "false".equalsIgnoreCase(readOnlyAttribute);
    }

    public String getFeedbackRequestTitleValue() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");
        return modalContainer.findElement(By.id("requestTitle")).getAttribute("value");
    }

    public boolean isFeedbackRequestWithTitlePresent(String title) {
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.linkText(title), "modalContainer");
        return driver.findElement(By.linkText(title)).isDisplayed();
    }

    public void clickSubmitFeedbackRequestButton() {
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.cssSelector(".btn-success"), "asutamiseks button");
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
        wait.sleep(2000);
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

    public void clickOnAddNewDocumentationUrlButton() {
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

    public void uploadNewFileDocumentFile(String fileName) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        WebElement linkTypeElement = modalContainer.findElement(By.id("fileType"));
        linkTypeElement.click();
        new Select(linkTypeElement).selectByValue("DOC_TYPE_ISKE_ACT");

        //upload test data file
        makeElementVisible("dataFile");
        String path = Utils.getFileResourcePath(new File(fileName));
        modalContainer.findElement(By.id("dataFile")).sendKeys(path);
        modalContainer.findElement(By.cssSelector(".col-12 > .btn")).click();
        wait.sleep(3000);

    }

    public boolean isUserCanEditLastUploadedDocument() {
        modalContainer.findElement(By.cssSelector(".expandable-block:last-child .mr-1")).click();
        wait.sleep(1000);
        boolean filenameInputVisible = modalContainer.findElement(By.cssSelector(".expandable-block:last-child .form-group:nth-child(1)")).isDisplayed();
        modalContainer.findElement(By.cssSelector(".btn-success")).click();
        return filenameInputVisible;
    }

    public void enterNewLegalActInfo(String url, String title) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        modalContainer.findElement(By.id("url")).sendKeys(url);
        modalContainer.findElement(By.id("name")).sendKeys(title);
        WebElement legalTypeSelect = modalContainer.findElement(By.id("type"));
        legalTypeSelect.click();
        new Select(legalTypeSelect).selectByValue("LEGAL_TYPE_STATUTE");
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

    public void addDataObjectFileAndUrlToInfosystem(String dataObject, String fileName, String url, String urlName) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        //add data object
        modalContainer.findElement(By.id("object")).sendKeys(dataObject);
        modalContainer.findElement(By.cssSelector(".col-2 > .btn")).click();

        //upload test data file
        makeElementVisible("dataFile");
        String path = Utils.getFileResourcePath(new File(fileName));
        modalContainer.findElement(By.id("dataFile")).sendKeys(path);
        wait.forElementToBeClickable(DISPLAY_ELEMENT_TIMEOUT, By.id("url"), "spinner");

        //add url
        modalContainer.findElement(By.id("url")).sendKeys(url);
        modalContainer.findElement(By.id("name")).sendKeys(urlName);
        modalContainer.findElement(By.cssSelector(".col-12 > .btn")).click();

        modalContainer.findElement(By.cssSelector(".btn-success")).click();
        wait.sleep(2000);
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

    public void removeDataObjectFileAndUrl(String dataObject, String fileName, String urlName) {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalContainer, "modalContainer");

        //remove data object
        WebElement dataObjectTr = modalContainer.findElement(By.cssSelector(".details-list-table:nth-child(1)"))
                .findElements(By.tagName("tr"))
                .stream()
                .filter(tr -> tr.findElement(By.cssSelector("td:nth-child(2)")).getText().equals(dataObject))
                .findFirst().get();

        dataObjectTr.findElement(By.className("btn-danger")).click();

        //remove file
        findTrWithText(fileName).findElement(By.className("btn-danger")).click();

        //remove url
        findTrWithText(urlName).findElement(By.className("btn-danger")).click();

        modalContainer.findElement(By.cssSelector(".btn-success")).click();
    }

    private WebElement findTrWithText(String text) {
        return modalContainer.findElement(By.cssSelector(".dataTables_wrapper:nth-child(4) > .details-list-table"))
                .findElements(By.tagName("tr"))
                .stream()
                .filter(tr -> tr.findElement(By.cssSelector("td:nth-child(2) > a")).getText().equals(text))
                .findFirst().get();
    }

    public void clickOnEditAssociationsButton() {
        this.AssociationsEditButton.click();
    }

    public void navigateToAssociations() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, navigationToAssociations, "navigationToAssociations");
        this.navigationToAssociations.click();
        wait.sleep(2000);
    }

    public void enterShortNameForAssociatedSystem(String shortName) {
        wait.forPresenceOfElements(5, By.tagName("ngb-modal-window"), "modal");
        this.modalAssociationsShortName.sendKeys(shortName);
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, modalAssociationsAutoOption, "modalAssociationsAutoOption");
        this.modalAssociationsAutoOption.click();
    }

    public void selectAssociationTypeAndSave() {
        this.associationTypeDropDown.click();
        new Select(associationTypeDropDown).selectByValue("SUB_SYSTEM");
        this.saveNewAssociation.click();
    }

    public String getNewAssociatedInfosystemInModal() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, newAssociatedInfosystemInModal, "newAssociatedInfosystemInModal");
        return this.newAssociatedInfosystemInModal.getText();
    }

    public void clickNewAssociatedInfosystemInModal() {
        this.newAssociatedInfosystemInModal.click();
    }

    public void createNewTab() {
        driver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL,"t");
        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    public String getAssociatedInfosystemInDetail() {
        try {
            return this.newAssociatedInfosystemInDetail.getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public void clickAssociatedInfosystemInDetail() {
        this.newAssociatedInfosystemInDetail.click();
    }

    public void goToPreviousPage() {
        driver.navigate().back();
    }

    public void deleteAssociation() {
        this.deleteAssociationButton.click();
    }

    public void switchTabs(String tab) {
        driver.switchTo().window(tabs.get(Integer.parseInt(tab)));
    }

    public void refreshPage() {
        driver.navigate().refresh();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void clickLogOut() {
        wait.forElementToBeDisplayed(DISPLAY_ELEMENT_TIMEOUT, logOutButton, "logOutButton");
        this.logOutButton.click();
    }

    public void clickFeedbackTopic(String topic) {
        wait.forPresenceOfElements(DISPLAY_ELEMENT_TIMEOUT, By.linkText(topic), "feedbackTopic");
        driver.findElement(By.linkText(topic)).click();
    }

}
