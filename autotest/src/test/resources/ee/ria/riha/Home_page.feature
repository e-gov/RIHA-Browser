Feature: Home page

  Scenario Outline: Check page display
    Given A user navigates to HomePage "<url>"
    Then logo is displayed
    And page title is "Avaleht - Riigi infosüsteemi haldussüsteem RIHA"

    Examples:
      | url |
      | https://riha-browser-ik.ci.kit |