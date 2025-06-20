@password-management @security
Feature: Fonctionnalité de changement de mot de passe

  Scenario: Soumission du formulaire avec des champs vides
    Given utilisateur est connecté
    When il navigue vers la page de changement de mot de passe
    And il clique sur le bouton de validation sans remplir les champs
    Then trois messages d'erreur doivent s'afficher

  Scenario: Mot de passe et confirmation différents
    Given utilisateur est connecté
    When il navigue vers la page de changement de mot de passe
    And il saisit l'ancien mot de passe "<ancienMotDePasse123>"
    And il saisit le nouveau mot de passe "<newMotDePasse123>"
    And il saisit la confirmation du mot de passe "<newMotDePasse12345>"
    When il clique sur le bouton de validation du mot de passe
    Then un message d'erreur doit s'afficher pour la non-concordance des mots de passe
