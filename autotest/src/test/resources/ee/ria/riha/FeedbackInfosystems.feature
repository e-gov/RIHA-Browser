@RIHAKB-710
Feature: Infosystems feedback requesting

  Background:
  Given User is logged in to "/" as "60001019906" using tel. number "00000766"
    And HomePage: user opens 'my organization infosystems' page
    And MyInfosystemsPage: user knows the number of last created infosystem with prefix "autotest"
    And BackgroundSteps: user creates system with name "autotest", shortName "feedback-test", purpose "purpose test", dataObject "dataObject", url "https://ria.ee", urlName "Ria web", technicalDocumentationUrl "https://ria.ee", technicalDocumentationLink "riaDok", contactName "contactName", email "test@test.ee", legalUrl "https://riigiteataja.ee", legalName "riigiteataja"

  Scenario: Owner of the system can request feedback (RIHAKB-463)

    When InfosytemPage: feedback button is clicked
    And InfosytemPage: request feedback comment "test comment" is submitted