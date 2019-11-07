Feature: My organization infosystems page functionality

  Background:
    Given User is logged in to "https://riha-browser-ik.ci.kit" as "60001019906" using tel. number "00000766"

  Scenario: User can search infosystem by keyword (RIHAKB-425)
    Given User opens 'my organization infosystems page'
    And user clicks on 'täpsusta otsingut'
    And user enters search topic "x-tee alamsüsteem" and presses 'enter'
    Then All displayed infosystems on 'my infosystems' page have "x-tee alamsüsteem" associated topic
    When user clicks on "riha-test" infosystem link
    Then "x-tee alamsüsteem" topic is present in associated topics list
    When user clicks on "x-tee alamsüsteem" topic
    Then All displayed infosystems on 'infosystems' page have "x-tee alamsüsteem" associated topic
    And topic input has "x-tee alamsüsteem" text

#  Scenario: User can create new infosystem (RIHAKB-340)
#    Given User opens 'my organization infosystems page'
#    And user clicks 'Lisa uus'
#    And user enters name as "" short name as "" and purpose as ""