Feature: My organization infosystems page functionality

  Background:
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"

  Scenario: User can create new infosystem (RIHAKB-340)
    Given HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user knows the number of last created infosystem with prefix "autotest"
    And MyInfosystemsPage: user clicks 'Lisa uus'
    And MyInfosystemsPage: user enters name as "RIHA autotest number" short name as "autotest" increasing last number and purpose as "Testida RIHA funktsionaalsust"
    Then InfosytemPage: infosystem creation purpose is "Testida RIHA funktsionaalsust"