@RIHAKB-710
Feature: Infosystems feedback requesting

  Background:
  Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user knows the number of last created infosystem with prefix "riha-auto-test"

  Scenario: Owner of the system can request feedback (RIHAKB-463)
    Given BackgroundSteps: user creates system with name "riha-auto-test", shortName "riha-auto-test", purpose "purpose test", dataObject "dataObject", url "https://ria.ee", urlName "Ria web", technicalDocumentationUrl "https://ria.ee", technicalDocumentationLink "riaDok", contactName "contactName", email "test@test.ee", legalUrl "https://riigiteataja.ee", legalName "riigiteataja"
    When InfosytemPage: feedback button is clicked
    And InfosytemPage: feedback of type "KASUTUSELE_VOTMISEKS" with comment "test comment" is requested
    Then InfosystemPage: feedback form title is "Infosüsteemil puudub kasutusele võtmise kooskõlastus" and is editable "false"
    And InfosytemPage: feedback request submit button is clicked
    Then InfosystemPage: feedback request with title "Infosüsteemil puudub kasutusele võtmise kooskõlastus" is saved

  Scenario: Approver can give feedback to infosystem  (RIHAKB-342)
    Given MyInfosystemsPage: user select first found infosystem
    When InfosytemPage: feedback button is clicked
    And InfosytemPage: approver gives feedback with tile "test hinnang" and comment "test kommentaar"
    And InfosytemPage: feedback request submit button is clicked
    Then InfosytemPage: issue with title "test hinnang" is visible in open issues list



