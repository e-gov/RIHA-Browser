package ee.ria.riha.infosystempage;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.InfosystemPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class InfosystemPageSteps {
    private InfosystemPage infosystemPage;

    public InfosystemPageSteps() {
        infosystemPage = Setup.pageFactory.getPage(InfosystemPage.class);
    }

    @When("InfosytemPage: user clicks on {string} topic")
    public void userClicksOnTopic(String topic) {
        infosystemPage.clickOnTopic(topic);
    }

    @Then("InfosytemPage: {string} topic is present in associated topics list")
    public void topicIsPresentInAssociatedTopicsList(String topic) {
        Assert.assertTrue("Associated topic list doen't contain topic " + topic, infosystemPage.getAssociatedTopicsList().contains(topic.toUpperCase()));
    }
}
