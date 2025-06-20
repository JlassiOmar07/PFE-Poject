@single-transfer @financial
Feature: Virement Unitaire vers bénéficiaire


  Scenario: Effectuer un virement multi-devise avec des données valides
    Given l'utilisateur est connecté à UIB acceuil
    When il navigue vers créer un virment unitaire page
    And l'utilisateur sélectionne Compte à débiter
    And l'utilisateur sélectionne Compte à créditer
    And il saisit le montant "500.00"
    And il clique sur Valider
    And il entre le code secret
    Then un message de confirmation doit s'afficher


