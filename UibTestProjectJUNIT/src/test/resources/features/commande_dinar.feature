@dinar-order @financial
Feature: Commande de dinars avec des données valides

  Ce scénario vérifie le bon fonctionnement du processus de commande de dinars
  dans le cas d’une saisie complète et d’un cas d’erreur lié à des champs manquants.


  Scenario: Soumettre une commande de dinars avec des informations valides
    Given l'utilisateur est connecté et se trouve sur la page d'accueil
    When navigation vers la section de commande de dinars
    And ouverture du formulaire de nouvelle demande
    And sélection d’un compte, saisie du montant et choix d’une date
    And soumission de la commande
    Then validation réussie et absence de message d’erreur

  Scenario: Tentative de soumission avec des champs obligatoires non renseignés
    Given l'utilisateur est connecté et se trouve sur la page d'accueil
    When navigation vers la section de commande de dinars
    And ouverture du formulaire de nouvelle demande
    And sélection d’un compte uniquement
    And soumission de la commande sans
    Then affichage d’un message d’erreur signalant les champs manquants