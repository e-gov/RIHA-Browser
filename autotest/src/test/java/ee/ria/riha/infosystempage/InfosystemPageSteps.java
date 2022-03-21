package ee.ria.riha.infosystempage;

import ee.ria.riha.driver.*;
import ee.ria.riha.pages.*;
import io.cucumber.java.en.*;
import org.junit.*;

import java.util.stream.*;

import static ee.ria.riha.BackgroundSteps.*;
import static ee.ria.riha.context.ScenarioContext.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

public class InfosystemPageSteps {
    private InfosystemPage infosystemPage;

    public InfosystemPageSteps() {
        infosystemPage = Setup.pageFactory.getPage(InfosystemPage.class);
    }

    @When("InfosytemPage: user clicks on {string} topic")
    public void userClicksOnTopic(String topic) {
        infosystemPage.clickOnTopic(topic);
    }

    @And("InfosystemPage: user clicks on 'edit' button")
    public void userClicksOnEditButton() {
        infosystemPage.clickEditButton();
    }

    @And("InfosystemPage: user clicks on 'edit contacts' button")
    public void userClicksOnEditContactsButton() {
        infosystemPage.clickEditContactsButton();
    }

    @And("InfosystemPage: user clicks on 'edit documentation' button")
    public void userClicksOnEditDocumentationButton() {
        infosystemPage.clickEditDocumentationButton();
    }

    @And("InfosystemPage: user clicks on 'edit data' button")
    public void userClicksOnEditDataButton() {
        infosystemPage.clickEditDataButton();
    }

    @And("InfosystemPage: user adds data object {string}, data file {string} and url {string} - {string} to infosystem")
    public void userAddsDataObjecFileAndUrlToInfosystem(String dataObject, String fileName, String url, String urlName) {
        infosystemPage.addDataObjectFileAndUrlToInfosystem(dataObject, fileName, url, urlName);
    }

    @And("InfosystemPage: user removes data object {string}, data file {string} and url {string}")
    public void userRemovesDataObjectAndUrl(String dataObject, String fileName, String urlName) {
        infosystemPage.removeDataObjectFileAndUrl(dataObject, fileName, urlName);
    }

    @And("InfosystemPage: user changes general description by adding {string} to all fields")
    public void userChangesGeneralDescription(String suffix) {
        infosystemPage.changeGeneralDescription(suffix);
    }

    @And("InfosystemPage: user reverts changes in general description")
    public void userRevertsChangesInGeneralDescription() {
        infosystemPage.revertChangesInGeneralDescription();
    }

    @And("InfosystemPage: user enters contact name {string} and email {string}")
    public void userEntersContactNameAndEmail(String name, String email) {
        infosystemPage.enterContactNameAndEmail(name, email);
    }

    @And("InfosystemPage: user clicks on 'add new URL' button")
    public void userClicksOnAddNewUrlButton() {
        infosystemPage.clickOnAddNewDocumentationUrlButton();
    }

    @And("InfosystemPage: user enters new technical documentation link {string} with name {string}")
    public void userEntersNewTechnicalDocumentationLink(String url, String name) {
        infosystemPage.enterNewTechnicalDocumentationLink(url, name);
    }

    @And("InfosystemPage: user removes link to technical documentation with name {string}")
    public void userRemovesLinkToTechnicalDocumentation(String name) {
        infosystemPage.removeLinkToTechnicalDocumentation(name);
    }

    @And("InfosystemPage: user clicks on 'upload file' button")
    public void userClicksOnUploadNewFileButton() {
        infosystemPage.userClicksOnUploadNewFileButton();
    }

    @And("InfosystemPage: user adds documentation file")
    public void uploadDocumentationFile() {
        infosystemPage.uploadNewFileDocumentFile(FILE_UPLOAD_NAME);
    }

    @Then("InfosystemPage: last uploaded file info is editable")
    public void checkIfLastUploadedDocIsEditable() {
        assertTrue("last uploaded file is not editable", infosystemPage.isUserCanEditLastUploadedDocument());
    }

    @And("InfosystemPage: last uploaded date is displayed in the documentation section")
    public void checkIfLastUploadedDateIsDisplayed() {
        assertTrue("last uploaded file is not editable", infosystemPage.isUploadedDateDisplayedOnTheLastUploadedDocument());
    }


    @And("InfosystemPage: user enters new topic {string}")
    public void userEntersNewTopic(String topic) {
        infosystemPage.enterNewTopic(topic);
    }

    @And("InfosystemPage: user removes topic {string}")
    public void userRemovesTopic(String topic) {
        infosystemPage.removeTopic(topic);
    }

    @And("InfosystemPage: user clicks on 'delete contacts' button")
    public void userDeletesContact() {
        infosystemPage.deleteContact();
    }

    @And("InfosystemPage: user clicks on 'save' button")
    public void userClicksOnSaveButton() {
        infosystemPage.clickSaveButton();
    }

    @Then("InfosytemPage: {string} topic is present in associated topics list")
    public void topicIsPresentInAssociatedTopicsList(String topic) {
        assertTrue("Associated topic list doesn't contain topic " + topic, infosystemPage.getAssociatedTopicsList().contains(topic.toUpperCase()));
    }

    @When("InfosytemPage: feedback button is clicked")
    public void clickFeedbackRequestButton() {
        infosystemPage.clickRequestFeedbackButton();
    }

    @And("InfosytemPage: feedback of type {string} with comment {string} is requested")
    public void submitFeedbackRequest(String feedbackType, String feedbackComment) {
        infosystemPage.requestFeedback(InfosystemPage.FeedbackType.valueOf(feedbackType), feedbackComment);
    }

    @And("InfosytemPage: user adds issue with title {string} and comment {string} to the system")
    public void addFeedbackComment(String title, String comment) {
        infosystemPage.createIssue(title, comment);
    }

    @Then("InfosystemPage: issue is saved")
    public void checkThatFeedbackWasSaved() {
        assertTrue(infosystemPage.isIssueSaved());
    }

    @And("InfosytemPage: approver gives feedback with tile {string} and comment {string}")
    public void approverGivesFeedbackWithComment(String title, String comment) {
        infosystemPage.giveFeedback(title, comment);
    }

    @Then("InfosystemPage: feedback form title is {string} and is editable {string}")
    public void feedbackFormTitleIsNotEditable(String title, String editable) {
        assertThat(infosystemPage.getFeedbackRequestTitleValue(), is(title));
        assertThat(infosystemPage.isFeedbackRequestTitleEditable(), is(Boolean.valueOf(editable)));
    }

    @Then("InfosystemPage: feedback request with title {string} is saved")
    public void checkCreationOfFeedbackRequest(String title) {
        assertThat(infosystemPage.isFeedbackRequestWithTitlePresent(title), is(true));
    }

    @And("InfosytemPage: feedback request submit button is clicked")
    public void clickSubmitFeedbackRequestButton() {
        infosystemPage.clickSubmitFeedbackRequestButton();
    }

    @Then("InfosystemPage: link to technical documentation with name {string} presents in 'documentation' block")
    public void linkToTechnicalDocumentationPresentsInDocBlock(String linkName) {
        assertTrue("Documentation block doesn't have documentation link " + linkName,
                infosystemPage.getTechDocUrls().contains(linkName));
    }

    @Then("InfosystemPage: link to technical documentation with name {string} not present in 'documentation' block")
    public void linkToTechnicalDocumentationNotPresentInDocBlock(String linkName) {
        assertFalse("Documentation block have documentation link " + linkName,
                Stream.of(infosystemPage.getTechDocUrls().split(","))
                        .anyMatch(techDocUrl -> techDocUrl.equalsIgnoreCase(linkName)));
    }

    @Then("InfosytemPage: {string} topic is not present in associated topics list")
    public void topicIsNotPresentInAssociatedTopicsList(String topic) {
        assertFalse("Associated topic list contains topic " + topic,
                Stream.of(infosystemPage.getAssociatedTopicsList().split(","))
                        .anyMatch(associatedTopic -> associatedTopic.equalsIgnoreCase(topic)));
    }

    @Then("InfosystemPage: data object {string}, data file {string} and url {string} present in 'data' block")
    public void dataObjectFileAndUrlPresentInDataBlock(String dataObject, String fileName, String urlName) {
        assertTrue("Data block doesn't contain data object " + dataObject, infosystemPage.getDataObjects().contains(dataObject));
        assertTrue("Data block doesn't contain data file " + dataObject, infosystemPage.getDataUrls().contains(fileName));
        assertTrue("Data block doesn't contain url " + urlName, infosystemPage.getDataUrls().contains(urlName));
    }

    @Then("InfosystemPage: data object {string}, data file {string} and url {string} not present in 'data' block")
    public void dataObjectFileAndUrlNotPresentInDataBlock(String dataObject, String fileName, String urlName) {
        assertTrue("Data block contains data object " + dataObject, infosystemPage.getEmptyDataTable());

        assertTrue("Data block contains file " + fileName, infosystemPage.getEmptyUrlTable());

        assertTrue("Data block contains url " + urlName, infosystemPage.getEmptyUrlTable());
    }

    @Then("InfosytemPage: infosystem creation purpose is {string}")
    public void infosystemCreationPurposeIs(String purpose) {
        assertTrue("Infosystem creation purpose is not " + purpose, purpose.equalsIgnoreCase(infosystemPage.getPurposeText()));
    }

    @Then("InfosytemPage: issue with title {string} is visible in open issues list")
    public void issueIsVisibleInOpenIssuesList(String title) {
        assertTrue("Issue with title " + title + " is not visible in open issues list", infosystemPage.getOpenIssues().contains(title));
    }

    @Then("InfosytemPage: infosystem short name, name, creation purpose and homepage url end with {string}")
    public void infosystemShortNameNameAndCreationPurposeEndWith(String suffix) {
        assertTrue("Infosystem name doesn't end with " + suffix, infosystemPage.getName().endsWith(suffix));
        assertTrue("Infosystem short name doesn't end with " + suffix, infosystemPage.getShortName().endsWith(suffix));
        assertTrue("Infosystem creation purpose doesn't end with " + suffix, infosystemPage.getPurposeText().endsWith(suffix));
        assertTrue("Infosystem homepage doesn't end with " + suffix, infosystemPage.getHomepage().endsWith(suffix));
    }

    @Then("InfosytemPage: infosystem general description is restored")
    public void infosystemGeneralDescriptionIsRestored() {
        assertTrue("Infosystem name is not restored ", infosystemPage.getScenarioContext().getFromContext(NAME_KEY).equalsIgnoreCase(infosystemPage.getName()));
        assertEquals("Infosystem short name is not restored ", infosystemPage.getScenarioContext().getFromContext(SHORT_NAME_KEY), infosystemPage.getShortName());
        assertEquals("Infosystem creation purpose is not restored ", infosystemPage.getScenarioContext().getFromContext(PURPOSE_KEY).trim(), infosystemPage.getPurposeText().trim());
        assertEquals("Infosystem homepage is not restored ", infosystemPage.getScenarioContext().getFromContext(HOMEPAGE_KEY), infosystemPage.getHomepage());
    }

    @Then("InfosystemPage: contact name {string} is visible in 'contacts' block")
    public void contactNameIsVisibleInContactsBlock(String name) {
        assertEquals("Infosystem doesn't have contact " + infosystemPage.getContactName(), name, infosystemPage.getContactName());
    }

    @Then("InfosystemPage: placeholder {string} is visible in 'contacts' block")
    public void placeholderIsVisibleInContactsBlock(String text) {
        assertEquals("Empty contacts placeholder is wrong", text, infosystemPage.getEmptyContactsPlaceholder());
    }

    @When("InfosytemPage: user clicks on 'Seosed süsteemidega' in the navigation bar")
    public void userNavigatesToAssociations() {
        this.infosystemPage.navigateToAssociations();
    }

    @Then("InfosystemPage: url will contain {string}")
    public void verifyUrlChange(String url) {
        Assert.assertTrue("Url does not contain " + url, this.infosystemPage.getCurrentUrl(true).contains(url));
    }

    @When("InfosystemPage: user clicks on edit in 'Seosed süsteemidega' block")
    public void userClicksEditInAssociationsBlock() {
        this.infosystemPage.clickOnEditAssociationsButton();
    }


    @And("InfosystemPage: user enters {string} in shortname input box in modal window and clicks on suggested name")
    public void userEntersShortNameInModal(String shortName) {
        this.infosystemPage.enterShortNameForAssociatedSystem(shortName);
    }

    @And("InfosystemPage: user selects 'alaminfosüsteem' as type and saves")
    public void userSelectsTypeAndSaves() {
        this.infosystemPage.selectAssociationTypeAndSave();
    }

    @Then("InfosystemPage: a new associated infosystem {string} appears as a clickable link")
    public void verifyNewAssociatedInfosystem(String name) {
        String newAssociatedInfosystemInModal = this.infosystemPage.getNewAssociatedInfosystemInModal().split(" -")[0];
        assertEquals("New associated infosystem is not " + name, newAssociatedInfosystemInModal, name);
    }


    @When("InfosystemPage: user clicks the new associated infosystem and is redirected to a new tab")
    public void userClicksAssociatedInfosystemInModal() {
        this.infosystemPage.clickNewAssociatedInfosystemInModal();
        this.infosystemPage.createNewTab();
    }

    @And("InfosystemPage: associated infosystem {string} will appear")
    public void verifySystemInDetailView(String name) {
        String associatedInfosystemInDetail = this.infosystemPage.getAssociatedInfosystemInDetail().split(" -")[0];
        assertEquals("New associated infosystem is not " + name, associatedInfosystemInDetail, name );
    }


    @When("InfosystemPage: user clicks on said associated infosystem and new detail view page opens")
    public void userClicksAssociatedInfosystemInDetail() {
        this.infosystemPage.clickAssociatedInfosystemInDetail();
    }


    @And("InfosystemPage: user goes back to last page")
    public void userGoesToLastPage() {
        this.infosystemPage.goToPreviousPage();
    }

    @And("InfosystemPage: user goes back to the last tab {string}")
    public void userGoesToLastTab(String tab) {
        this.infosystemPage.switchTabs(tab);
    }

    @And("InfosystemPage: user deletes infosystem association")
    public void userDeletesAssociation() {
        this.infosystemPage.deleteAssociation();
    }


    @Then("InfosystemPage: first infosystem is not listed as associated")
    public void verifyAssociation() {
        assertEquals("Infosystem association was still found!", "", this.infosystemPage.getAssociatedInfosystemInDetail());
    }

    @When("InfosystemPage: user refreshes current page")
    public void refreshCurrentPage() {
        this.infosystemPage.refreshPage();
    }

    @When("InfosystemPage: user logs out of organization")
    public void logOut() {
        this.infosystemPage.clickLogOut();
    }

    @And("InfosystemPage: user clicks on feedback topic {string}")
    public void userClicksOnFeedbackTopic(String topic) {
        infosystemPage.clickFeedbackTopic(topic);
    }

    @And("InfosystemPage: user adds feedback with type {string} and comment {string}")
    public void userAddsFeedbackWithTypeAndComment(String type, String comment) {
        infosystemPage.userAddsFeedbackWithTypeAndComment(type, comment);
    }

}
