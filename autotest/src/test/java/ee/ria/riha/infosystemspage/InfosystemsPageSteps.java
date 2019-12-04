package ee.ria.riha.infosystemspage;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.InfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertTrue;

public class InfosystemsPageSteps {
    private InfosystemsPage infosystemsPage;

    public InfosystemsPageSteps() {
        infosystemsPage = Setup.pageFactory.getPage(InfosystemsPage.class);
    }

    @And("InfosystemsPage: user clicks on 'täpsusta otsingut'")
    public void userClicksOnDetailedSearchLink() {
        infosystemsPage.openDetailedSearchForm();
    }

    @And("InfosytemsPage: topic input has {string} text")
    public void topicInputHasText(String text) {
        assertTrue("Wrong text in topic input " + infosystemsPage.getTopicInputText(), text.equalsIgnoreCase(infosystemsPage.getTopicInputText()));
    }

    @And("InfosystemsPage: user enters 'owner name' {string}, 'name' {string} and 'search topic' {string} and presses 'enter'")
    public void userSearchesByTopicOwnerNameAndName(String ownerName, String name, String topic) {
        infosystemsPage.searchByOwnerNameAndTopic(ownerName, name, topic);
    }

    @Then("InfosytemsPage: all displayed infosystems on 'infosystems' page have {string} associated topic")
    public void allDisplayedInfosystemsHaveTopic(String topic) {
        infosystemsPage.getFoundInfosystemsTopics().forEach(topics -> assertTrue("Found infosystem desn't have associated topic " + topic, topics.contains(topic.toUpperCase())));
    }

    @Then("InfosystemsPage: URL contains 'ownerName={string}', 'name={string}' and 'topic={string}' elements")
    public void urlContainsOwnerNameAndtopicElements(String ownerName, String name, String topic) {
        assertTrue("Search URL doesn't contain 'ownerName' element", infosystemsPage.getCurrentUrl().contains("ownerName=" + ownerName));
        assertTrue("Search URL doesn't contain 'name' element", infosystemsPage.getCurrentUrl().contains("name=" + name));
        assertTrue("Search URL doesn't contain 'topic' element", infosystemsPage.getCurrentUrl().contains("topic=" + topic));
    }

    @Then("InfosytemsPage: 'owner' input has {string} text, 'name' input has {string} text and 'topic' input has {string} text")
    public void inputsHaveAppropriateText(String ownerName, String name, String topic) {
        assertTrue("Wrong text in owner input " + infosystemsPage.getTopicInputText(), ownerName.equalsIgnoreCase(infosystemsPage.getOwnerInputText()));
        assertTrue("Wrong text in name input " + infosystemsPage.getTopicInputText(), name.equalsIgnoreCase(infosystemsPage.getNameInputText()));
        assertTrue("Wrong text in topic input " + infosystemsPage.getTopicInputText(), topic.equalsIgnoreCase(infosystemsPage.getTopicInputText()));
    }

    @And("InfosytemsPage: found infosystem with short name {string}")
    public void foundInfosystemHasShortName(String shortName) {
        assertTrue("Infosystem with short name " + shortName + " not found", infosystemsPage.getFoundInfosystemsShortName().stream().anyMatch(shortName::equalsIgnoreCase));
    }

}
