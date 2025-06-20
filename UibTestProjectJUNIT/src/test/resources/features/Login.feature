@authentication @security
Feature: Connexion au portail UIB

  Scenario: Tentative de connexion avec des identifiants invalides
    Given L'utilisateur est sur la page de connexion PP
    When Il saisit l'identifiant "<user123>"
    And Il saisit un OTP invalide
    And Il clique sur le bouton Se connecter
    Then Un message d'erreur apparaît
