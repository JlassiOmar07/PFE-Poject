@currency-order @financial
Feature: Commande de devise avec des données valides

  Scenario: Effectuer une commande de devise avec des données valides
    Given l'utilisateur UIB est sur la page d'accueil après s'être connecté
    When accède à la page de commande de devises
    And click sur le bouton Nouvelle demande
    And sélectionne un compte
    And choisit une devise
    And saisit le montant
    And sélectionne le motif
    And sélectionne la date
    And click sur le bouton de validation
    Then la commande est envoyée aucune erreur ne s'affiche


  Scenario: L'utilisateur soumet une demande de chèque sans fournir toutes les données nécessaires
    Given l'utilisateur UIB est sur la page d'accueil après s'être connecté
    When accède à la page de commande de devises
    And click sur le bouton Nouvelle demande
    And sélectionne un compte
    And choisit une devise
    And saisit le montant
    And click sur le bouton de validation
    Then un message d'erreur s'affiche omar









