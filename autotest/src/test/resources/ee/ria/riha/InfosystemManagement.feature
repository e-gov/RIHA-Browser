@RIHAKB-710
Feature: My organization infosystems page functionality

  Background:
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page

  Scenario: User can create new infosystem (RIHAKB-340)
    Given MyInfosystemsPage: user knows the number of last created infosystem with prefix "autotest"
    And MyInfosystemsPage: user clicks 'Lisa uus'
    And MyInfosystemsPage: user enters name as "RIHA autotest number" short name as "autotest" increasing last number and purpose as "Testida RIHA funktsionaalsust"
    Then InfosytemPage: infosystem creation purpose is "Testida RIHA funktsionaalsust"

  Scenario: User can change general infosystem description (RIHAKB-370)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    And InfosystemPage: user clicks on 'edit' button
    And InfosystemPage: user changes general description by adding "1" to all fields
    And InfosystemPage: user clicks on 'save' button
    Then InfosytemPage: infosystem short name, name, creation purpose and homepage url end with "1"
    And InfosystemPage: user clicks on 'edit' button
    And InfosystemPage: user reverts changes in general description
    And InfosystemPage: user clicks on 'save' button
    Then InfosytemPage: infosystem general description is restored

  Scenario: User can enter and edit infosystem topic (RIHAKB-324)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    And InfosystemPage: user clicks on 'edit' button
    And InfosystemPage: user enters new topic "test"
    And InfosystemPage: user clicks on 'save' button
    Then InfosytemPage: "test" topic is present in associated topics list
    And InfosystemPage: user clicks on 'edit' button
    And InfosystemPage: user removes topic "test"
    And InfosystemPage: user clicks on 'save' button
    Then InfosytemPage: "test" topic is not present in associated topics list

  Scenario: User can enter URL to infosystem technical documentation (RIHAKB-355)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    And InfosystemPage: user clicks on 'edit documentation' button
    And InfosystemPage: user clicks on 'add new URL' button
    And InfosystemPage: user enters new technical documentation link "https://www.riha.ee" with name "RIHA veebileht"
    Then InfosystemPage: link to technical documentation with name "RIHA veebileht" presents in 'documentation' block
    And InfosystemPage: user clicks on 'edit documentation' button
    And InfosystemPage: user removes link to technical documentation with name "RIHA veebileht"
    Then InfosystemPage: link to technical documentation with name "RIHA veebileht" not present in 'documentation' block

  Scenario: User can add infosystem data (RIHAKB-356)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    And InfosystemPage: user clicks on 'edit data' button
    And InfosystemPage: user adds data object "Minu andmeobjekti", data file "test.txt" and url "https://www.riha.ee" - "RIHA testimine" to infosystem
    Then InfosystemPage: data object "Minu andmeobjekti", data file "test.txt" and url "RIHA testimine" present in 'data' block
    And InfosystemPage: user clicks on 'edit data' button
    And InfosystemPage: user removes data object "Minu andmeobjekti", data file "test.txt" and url "RIHA testimine"
    Then InfosystemPage: data object "Minu andmeobjekti", data file "test.txt" and url "RIHA testimine" not present in 'data' block

  Scenario: User can add contact to infosystem (RIHAKB-357)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    And InfosystemPage: user clicks on 'edit contacts' button
    And InfosystemPage: user enters contact name "Marje Karu" and email "marje.karu@gmail.com"
    Then InfosystemPage: contact name "Marje Karu" is visible in 'contacts' block
    And InfosystemPage: user clicks on 'edit contacts' button
    And InfosystemPage: user clicks on 'delete contacts' button
    Then InfosystemPage: placeholder "Kontaktid puuduvad" is visible in 'contacts' block
