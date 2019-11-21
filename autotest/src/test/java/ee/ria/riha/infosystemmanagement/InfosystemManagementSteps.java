package ee.ria.riha.infosystemmanagement;

import ee.ria.riha.driver.Setup;
import ee.ria.riha.pages.MyInfosystemsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class InfosystemManagementSteps {
    private MyInfosystemsPage myInfosystemsPage;

    public InfosystemManagementSteps() {
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

    @And("MyInfosystemsPage: user enters name as {string} short name as {string} increasing last number and purpose as {string}")
    public void userEntersNameShortNameAndPurpose(String namePrefix, String shortNamePrefix, String purpose) {
        myInfosystemsPage.enterNameShortNameAndPurpose(namePrefix, shortNamePrefix, purpose);
    }

    @Given("MyInfosystemsPage: user knows the number of last created infosystem with prefix {string}")
    public void userKnowsTheNameOfLastCreatedInfosystem(String namePrefix) {
        myInfosystemsPage.enterSearchText(namePrefix);
        myInfosystemsPage.sortByShortNameDesc();
        myInfosystemsPage.saveFirstFoundInfosystemShortNameToScenarioContext();
    }

    @Given("MyInfosystemsPage: user select {string} infosystem")
    public void userSelectsInfosystem(String shortName) {
        myInfosystemsPage.enterSearchText(shortName);
        myInfosystemsPage.selectFirstFoundInfosystem();
    }

    @When("MyInfosystemsPage: user clicks on {string} infosystem link")
    public void userClicksOnIfosystemLink(String infosystem) {
        myInfosystemsPage.goToInfosystemPage(infosystem);
    }

    @Then("MyInfosystemsPage: all displayed infosystems on 'my infosystems' page have {string} associated topic")
    public void allDisplayedInfosystemsHaveTopic(String topic) {
        myInfosystemsPage.getFoundInfosystemsTopics().forEach(topics -> Assert.assertTrue("Found infosystem doesn't have associated topic " + topic, topics.contains(topic.toUpperCase())));
    }
}
