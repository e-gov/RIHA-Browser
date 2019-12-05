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

Scenario: Owner of the system with Approver role can leave comments (RIHAKB-464)
  Given BackgroundSteps: user creates system with name "riha-auto-test", shortName "riha-auto-test", purpose "purpose test", dataObject "dataObject", url "https://ria.ee", urlName "Ria web", technicalDocumentationUrl "https://ria.ee", technicalDocumentationLink "riaDok", contactName "contactName", email "test@test.ee", legalUrl "https://riigiteataja.ee", legalName "riigiteataja"
  When InfosytemPage: feedback button is clicked
  And InfosytemPage: feedback of type "KASUTUSELE_VOTMISEKS" with comment "test comment" is requested
  Then InfosystemPage: feedback form title is "Infosüsteemil puudub kasutusele võtmise kooskõlastus" and is editable "false"
  And InfosytemPage: feedback request submit button is clicked
  Then InfosystemPage: feedback request with title "Infosüsteemil puudub kasutusele võtmise kooskõlastus" is saved
  When InfosytemPage: feedback button is clicked
  And InfosytemPage: user adds issue with title "test title" and comment "this is a test comment" to the system
  And InfosytemPage: feedback request submit button is clicked
  Then InfosystemPage: issue is saved

  Scenario: Approver can give feedback to infosystem  (RIHAKB-342)
    Given MyInfosystemsPage: user select first found infosystem
    When InfosytemPage: feedback button is clicked
    And InfosytemPage: approver gives feedback with tile "test hinnang" and comment "test kommentaar"
    And InfosytemPage: feedback request submit button is clicked
    Then InfosytemPage: issue with title "test hinnang" is visible in open issues list

  Scenario: Approver can check for systems in need of feedback (RIHAKB-592)
    Given BackgroundSteps: user creates system with name "riha-auto-test", shortName "riha-auto-test", purpose "purpose test", dataObject "dataObject", url "https://ria.ee", urlName "Ria web", technicalDocumentationUrl "https://ria.ee", technicalDocumentationLink "riaDok", contactName "contactName", email "test@test.ee", legalUrl "https://riigiteataja.ee", legalName "riigiteataja"

    When InfosytemPage: feedback button is clicked
    And InfosytemPage: feedback of type "ANDMEKOOSSEISU_MUUTMISEKS" with comment "test" is requested
    And InfosytemPage: feedback request submit button is clicked
    Then InfosystemPage: feedback request with title "Infosüsteemil puudub andmekoosseisu muutmise kooskõlastus" is saved
    And HomePage: user opens Select Organization dialog
    And HomePage: user selects organization 'Testasutus 2'
    When HomePage: user clicks on evaluate button
    Then HomePage: option 'Kooskõlastamisel infosüsteemid' is visible in left menu
    Then HomePage: option 'Aktiivsed arutelud' is visible in left menu
    Then HomePage: table with headers 'Lühinimi,Infosüsteemi nimi,Kooskõlastamine,Otsuse tähtpäev' is visible
    And EvaluateSystemsPage: previously saved system is visible in required feedback table
    And EvaluateSystemsPage: previously saved system feedback deadline date is not red
    When EvaluateSystemsPage: previously saved system shortName link is clicked
    And  InfosystemPage: user clicks on feedback topic 'Infosüsteemil puudub andmekoosseisu muutmise kooskõlastus'
    And InfosystemPage: user adds feedback with type 'Kooskõlastan' and comment 'test comment on feedback form'
    And HomePage: user opens Select Organization dialog
    And HomePage: user selects organization 'Testasutus'
    When HomePage: user clicks on evaluate button
#  TODO!! last step fails because of missing roles
#    And EvaluateSystemsPage: previously saved system is not visible in required feedback table


