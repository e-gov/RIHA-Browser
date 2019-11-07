package ee.ria.riha.myinfosystemspage;

import ee.ria.riha.pages.MyInfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class MyInfosystemsPageSteps {
    MyInfosystemsPage myInfosystemsPage;

    public MyInfosystemsPageSteps() {
        this.myInfosystemsPage = new MyInfosystemsPage();
    }

    @And("^user clicks on 'täpsusta otsingut'$")
    public void userClicksOnDetailedSearchLink() {
        this.myInfosystemsPage.openDetailedSearchForm();
    }

    @And("^user enters search topic \"([^\"]*)\" and presses 'enter'$")
    public void userSearchesByTopic(String topic) throws Exception {
        this.myInfosystemsPage.searchByTopic(topic);
    }

    @And("^user clicks 'Lisa uus'$")
    public void userClicksCreateNew() {
        this.myInfosystemsPage.goToCreateNewInfosystemPage();
    }

    @And("^user enters name as \"([^\"]*)\" short name as \"([^\"]*)\" and purpose as \"([^\"]*)\"$")
    public void userEntersNameShortNameAndPurpose(String name, String shortName, String purpose) {
        this.myInfosystemsPage.enterNameSHortNameAndPurpose(name, shortName, purpose);
    }

    @When("^user clicks on \"([^\"]*)\" infosystem link$")
    public void userClicksOnIfosystemLink(String infosystem) {
        this.myInfosystemsPage.goToInfosystemPage(infosystem);
    }

    @Then("^All displayed infosystems on 'my infosystems' page have \"([^\"]*)\" associated topic$")
    public void allDisplayedInfosystemsHaveTopic(String topic) {
        this.myInfosystemsPage.getFoundInfosystemsTopics().forEach(topics -> Assert.assertTrue("Found infosystem desn't have associated topic " + topic, topics.contains(topic.toUpperCase())));
    }
}
