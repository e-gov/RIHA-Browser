package ee.ria.riha.infosystempage;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.InfosystemPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.stream.Stream;

import static ee.ria.riha.context.ScenarioContext.HOMEPAGE_KEY;
import static ee.ria.riha.context.ScenarioContext.NAME_KEY;
import static ee.ria.riha.context.ScenarioContext.PURPOSE_KEY;
import static ee.ria.riha.context.ScenarioContext.SHORT_NAME_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        infosystemPage.clickOnAddNewUrlButton();
    }

    @And("InfosystemPage: user enters new technical documentation link {string} with name {string}")
    public void userEntersNewTechnicalDocumentationLink(String url, String name) {
        infosystemPage.enterNewTechnicalDocumentationLink(url, name);
    }

    @And("InfosystemPage: user removes link to technical documentation with name {string}")
    public void userRemovesLinkToTechnicalDocumentation(String name) {
        infosystemPage.removeLinkToTechnicalDocumentation(name);
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
        assertFalse("Data block contains data object " + dataObject,
                Stream.of(infosystemPage.getDataObjects().split(",")).anyMatch(obj -> obj.equalsIgnoreCase(dataObject)));

        assertFalse("Data block contains file " + fileName,
                Stream.of(infosystemPage.getDataUrls().split(",")).anyMatch(url -> url.equalsIgnoreCase(fileName)));

        assertFalse("Data block contains url " + urlName,
                Stream.of(infosystemPage.getDataUrls().split(",")).anyMatch(url -> url.equalsIgnoreCase(urlName)));
    }

    @Then("InfosytemPage: infosystem creation purpose is {string}")
    public void infosystemCreationPurposeIs(String purpose) {
        assertTrue("Infosystem creation purpose is not " + purpose, purpose.equalsIgnoreCase(infosystemPage.getPurposeText()));
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
}
