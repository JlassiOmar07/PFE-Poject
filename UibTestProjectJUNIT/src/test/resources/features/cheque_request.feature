@cheque-request @financial
Feature: Demande de chéquier

  Scenario: Soumission d'une demande de chéquier avec des données valides
    Given utilisateur est connecté et se aller vers page d'accueil
    When utilisateur accède à la page de demande de chéquier
    And utilisateur sélectionne l'option pour une nouvelle demande de chéquier
    And utilisateur choisit un compte
    And utilisateur saisit un nom du bénéficiaire
    And utilisateur entre un agence
    And utilisateur indique un montant
    And utilisateur renseigne un motif
    And utilisateur soumet la demande en cliquant sur le bouton de validation
    Then un message d'erreur, "Une erreur est survenue!", doit être affiché


  Scenario: Affichage des messages d'erreur lorsque les champs sont vides
    Given utilisateur est connecté et se aller vers page d'accueil
    When utilisateur accède à la page de demande de chéquier
    And utilisateur sélectionne l'option pour une nouvelle demande de chéquier
    And utilisateur soumet la demande sans remplir les champs requis
    Then cinq messages d'erreur doivent apparaître, indiquant les champs manquants ou invalides
