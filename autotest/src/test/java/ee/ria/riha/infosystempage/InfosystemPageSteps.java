package ee.ria.riha.infosystempage;

import ee.ria.riha.pages.InfosystemPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class InfosystemPageSteps {
    private InfosystemPage infosystemPage;

    public InfosystemPageSteps() {
        this.infosystemPage = new InfosystemPage();
    }

    @When("^user clicks on \"([^\"]*)\" topic$")
    public void userClicksOnTopic(String topic) {
        this.infosystemPage.clickOnTopic(topic);
    }

    @Then("^\"([^\"]*)\" topic is present in associated topics list$")
    public void topicIsPresentInAssociatedTopicsList(String topic) {
        Assert.assertTrue("Associated topic list doen't contain topic " + topic, this.infosystemPage.getAssociatedTopicsList().contains(topic.toUpperCase()));
    }
}
