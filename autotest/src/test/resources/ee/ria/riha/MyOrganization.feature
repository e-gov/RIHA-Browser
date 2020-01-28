@RIHAKB-710
Feature: My organization listing feature

  Scenario: User can navigate to Minu organisatsioon listing page (RIHAKB-791)
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user clicks 'Minu organisatsioon'
    Then MyOrganizationPage: table with employees is displayed
    And MyOrganizationPage: table with discussions is sortable
