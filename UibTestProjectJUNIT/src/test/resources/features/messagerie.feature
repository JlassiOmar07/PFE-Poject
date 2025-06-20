@messaging @communication
Feature: Gestion de la messagerie

  Scenario: Envoyer un message avec tous les champs valides.
    Given l'utilisateur est connecté et sur la page de messagerie
    When un nouveau message est initié
    And le destinataire "Mon chargé d'affaires" est sélectionné
    And le sujet "Contact Technical Support" est sélectionné
    And le message "Bonjour, pouvez-vous m'aider ?" est saisi
    And un fichier valide est téléchargé
    And Cliquez sur le bouton Soumettre
    Then Un message de réussite devrait apparaître

  Scenario: Envoyer un message avec un fichier non autorisé
    Given l'utilisateur est connecté et sur la page de messagerie
    When un nouveau message est initié
    And le destinataire "Mon chargé d'affaires" est sélectionné
    And le sujet "Contact Technical Support" est sélectionné
    And le message "Bonjour, pouvez-vous m'aider ?" est saisi
    And un fichier non autorisé est téléchargé
    And un message d'erreur avec "Extension non autorisée" doit s'afficher

