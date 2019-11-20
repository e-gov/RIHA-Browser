Feature: Login

  Scenario Outline: Login to application (RIHAKB-308)
    Given HomePage: user navigates to page "<url>"
    And HomePage: user clicks on login button
    When LoginPage: user selects 'Mobiil-ID' tab
    And LoginPage: user enters 'Isikukood' "<personalCode>" and 'Telefoninumber' "<phoneNumber>"
    Then HomePage: home page is opened showing logged in user's name "<name>"

    Examples:
      | url | personalCode | phoneNumber | name |
      | / | 60001019906 | 00000766 | MARY ÄNN O’CONNEŽ-ŠUSLIK TESTNUMBER |