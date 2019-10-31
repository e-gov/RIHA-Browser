Feature: Login

  Scenario Outline: Login to application
    Given A user navigates to HomePage "<url>"
    And user clicks on login button
    When user selects 'Mobiil-ID' tab
    And user enters 'isikukood' "<personalCode>" and 'Telefoninumber' "<phoneNumber>"
    Then home page is opened showing logged in user's name "<name>"

    Examples:
      | url | personalCode | phoneNumber | name |
      | https://riha-browser-ik.ci.kit | 60001019906 | 00000766 | MARY ÄNN O’CONNEŽ-ŠUSLIK TESTNUMBER |