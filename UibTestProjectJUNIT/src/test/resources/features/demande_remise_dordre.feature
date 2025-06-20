@order-remittance @financial
Feature: Demande de remise d'ordre

  Scenario: Demande de remise avec des données valides
    Given l'utilisateur est connecté et sur la page de demande de remise d'ordre
    When le compte "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND" est sélectionné
    And la nature de remise "Virement domestique" est sélectionnée
    And le type de remise "Format CSV" est sélectionné
    And les informations de la remise sont saisies
    And le fichier est importé
    And le bouton de validation est cliqué
    Then la demande est soumise avec succès
