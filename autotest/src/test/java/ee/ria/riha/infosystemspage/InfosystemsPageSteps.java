package ee.ria.riha.infosystemspage;

import ee.ria.riha.pages.InfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class InfosystemsPageSteps {
    private InfosystemsPage infosystemsPage;

    public InfosystemsPageSteps() {
        this.infosystemsPage = new InfosystemsPage();
    }

    @And("^topic input has \"([^\"]*)\" text$")
    public void topicInputHasText(String text) {
        Assert.assertTrue("Wrong text in topic input " + infosystemsPage.getTopicInputText(), text.equalsIgnoreCase(infosystemsPage.getTopicInputText()));
    }

    @Then("^All displayed infosystems on 'infosystems' page have \"([^\"]*)\" associated topic$")
    public void allDisplayedInfosystemsHaveTopic(String topic) throws Exception {
        this.infosystemsPage.getFoundInfosystemsTopics().forEach(topics -> Assert.assertTrue("Found infosystem desn't have associated topic " + topic, topics.contains(topic.toUpperCase())));
    }
}
