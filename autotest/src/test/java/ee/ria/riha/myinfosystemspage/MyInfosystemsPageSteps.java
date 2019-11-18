package ee.ria.riha.myinfosystemspage;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.MyInfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class MyInfosystemsPageSteps {
    private MyInfosystemsPage myInfosystemsPage;

    public MyInfosystemsPageSteps() {
        myInfosystemsPage = Setup.pageFactory.getPage(MyInfosystemsPage.class);
    }

    @And("MyInfosystemsPage: user clicks on 'täpsusta otsingut'")
    public void userClicksOnDetailedSearchLink() {
        myInfosystemsPage.openDetailedSearchForm();
    }

    @And("MyInfosystemsPage: user enters search topic {string} and presses 'enter'")
    public void userSearchesByTopic(String topic) {
        myInfosystemsPage.searchByTopic(topic);
    }

    @And("MyInfosystemsPage: user clicks 'Lisa uus'")
    public void userClicksCreateNew() {
        myInfosystemsPage.goToCreateNewInfosystemPage();
    }

    @And("MyInfosystemsPage: user enters name as {string} short name as {string} and purpose as {string}")
    public void userEntersNameShortNameAndPurpose(String name, String shortName, String purpose) {
        myInfosystemsPage.enterNameShortNameAndPurpose(name, shortName, purpose);
    }

    @When("MyInfosystemsPage: user clicks on {string} infosystem link")
    public void userClicksOnIfosystemLink(String infosystem) {
        myInfosystemsPage.goToInfosystemPage(infosystem);
    }

    @Then("MyInfosystemsPage: all displayed infosystems on 'my infosystems' page have {string} associated topic")
    public void allDisplayedInfosystemsHaveTopic(String topic) {
        myInfosystemsPage.getFoundInfosystemsTopics().forEach(topics -> Assert.assertTrue("Found infosystem desn't have associated topic " + topic, topics.contains(topic.toUpperCase())));
    }
}
