Feature: Infosystems feedback requesting

  Background:
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page

  Scenario: User can add relation to infosystem (RIHAKB-423)
    Given MyInfosystemsPage: user select "riha-test" infosystem
    When InfosytemPage: user clicks on 'Seosed süsteemidega' in the navigation bar
    Then InfosystemPage: url will contain '/Infosüsteemid/Vaata/riha-test#seosed'
    When InfosystemPage: user clicks on edit in 'Seosed süsteemidega' block
    And InfosystemPage: user enters 'ummik' in shortname input box in modal window and clicks on suggested name
    And InfosystemPage: user selects 'alaminfosüsteem' as type and saves
    Then InfosystemPage: a new associated infosystem 'Ummikuregister (ummik.test)' appears as a clickable link
    When InfosystemPage: user clicks the new associated infosystem and is redirected to a new tab
    And InfosytemPage: user clicks on 'Seosed süsteemidega' in the navigation bar
    Then InfosystemPage: url will contain 'Infosüsteemid/Vaata/ummik.test#seosed'
    And InfosystemPage: associated infosystem 'riha-test (riha-test)' will appear
    When InfosystemPage: user clicks on said associated infosystem and new detail view page opens
    And InfosystemPage: user goes back to last page
    And InfosystemPage: user goes back to the last tab '0'
    And InfosystemPage: user deletes infosystem association
    And InfosystemPage: user goes back to the last tab '1'
    When InfosystemPage: user refreshes current page
    Then InfosystemPage: first infosystem is not listed as associated


