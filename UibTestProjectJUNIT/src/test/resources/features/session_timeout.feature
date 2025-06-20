@session-management @security @ignore
Feature: Gestion des sessions utilisateur

  Scenario: Redirection vers la page de login après expiration de session
    Given l'utilisateur est connecté à l'application
    When la session expire après '5' minutes d’inactivité
    Then l'utilisateur est redirigé vers la page de login avec le titre 'UIB Société Générale | MYBUSINESS'
