@RIHAKB-710
Feature: My feedbacks listing feature

  Scenario: User can navigate to Minu arutelu listing page (among his own infosystems) (RIHAKB-736)
    Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user clicks 'Minu arutelud'
    Then MyDiscussionsPage: table with discussions is displayed
    And MyDiscussionsPage: table with discussions is sortable
    Given MyDiscussionsPage: user clicks on discussion shortName link and remembers the shortName
    Then MyDiscussionsPage: user is located on the selected infosystem page
    And HomePage: user returns to the previous page
    Given MyDiscussionsPage: user clicks on discussion discussion title link and remembers the title
    Then MyDiscussionsPage: user navigates to the selected discussion popup