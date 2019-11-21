Feature: Search infosystems functionality

  Scenario: User can search infosystem by keyword (among his own infosystems) (RIHAKB-425)
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user clicks on 'täpsusta otsingut'
    And MyInfosystemsPage: user enters search topic "x-tee alamsüsteem" and presses 'enter'
    Then MyInfosystemsPage: all displayed infosystems on 'my infosystems' page have "x-tee alamsüsteem" associated topic
    When MyInfosystemsPage: user clicks on "riha-test" infosystem link
    Then InfosytemPage: "x-tee alamsüsteem" topic is present in associated topics list
    When InfosytemPage: user clicks on "x-tee alamsüsteem" topic
    Then InfosytemsPage: all displayed infosystems on 'infosystems' page have "x-tee alamsüsteem" associated topic
    And InfosytemsPage: topic input has "x-tee alamsüsteem" text

  Scenario: User can see infosystem search filter in URL (RIHAKB-424)
    Given HomePage: user navigates to page "/"
    And HomePage: user opens 'infosystems' page
    And InfosystemsPage: user clicks on 'täpsusta otsingut'
    And InfosystemsPage: user enters 'owner name' "riigi", 'name' "riigi" and 'search topic' "x-tee" and presses 'enter'
    Then InfosystemsPage: URL contains 'ownerName="riigi"', 'name="riigi"' and 'topic="x-tee"' elements
    When HomePage: user navigates to page "/Infosüsteemid?name=riigi&topic=x-tee&ownerName=riigi"
    Then InfosytemsPage: 'owner' input has "riigi" text, 'name' input has "riigi" text and 'topic' input has "x-tee" text
    And InfosytemsPage: found infosystem with short name "riha"