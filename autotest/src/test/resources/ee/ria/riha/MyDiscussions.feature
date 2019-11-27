@RIHAKB-710
Feature: My feedbacks listing feature

  Scenario: User can navigate to Minu arutelu listing page (among his own infosystems) (RIHAKB-736)
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user clicks 'Minu arutelud'
    Then MyDiscussionsPage: table with discussions is displayed
    And MyDiscussionsPage: table with discussions is sortable