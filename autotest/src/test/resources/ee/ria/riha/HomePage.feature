@RIHAKB-710
Feature: Home page functionality

  Background:
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"

  Scenario: User can select organization which he is representing in RIHA (RIHAKB-343)
    Given HomePage: user selects organization "Testasutus 2"
    Then HomePage: organization name "Testasutus 2" is shown next to user's name
    And HomePage: page title is "Avaleht - Riigi infosüsteemi haldussüsteem RIHA"