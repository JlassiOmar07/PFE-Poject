@standing-order @transfer
Feature: Virement Permanent

  Scenario: Création d'un virement permanent avec champs valide
    Given l'utilisateur est connecté à l'application MyBusiness XX
    When l'utilisateur clique sur "Créer un virement permanent"
    And l'utilisateur sélectionne un compte à débiter "Compte Adria"
    And l'utilisateur sélectionne un compte à créditer "Nicolas"
    And l'utilisateur sélectionne une fréquence "Mensuelle"
    And l'utilisateur saisit le montant "100"
    And l'utilisateur sélectionne la date de fin identique à la date d'exécution
    And l'utilisateur clique sur le bouton valider XX
    And l'utilisateur confirme le virement en saisissant le code secret
    Then un message de succès est affiché XX


