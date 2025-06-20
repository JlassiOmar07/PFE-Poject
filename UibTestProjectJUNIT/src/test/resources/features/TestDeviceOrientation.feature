@responsive @ui
Feature: Test de la mise en page responsive

  Scenario: Vérifier la mise en page sur différents appareils
    Given l'utilisateur est connecté à l'application MyBusiness YY
    When l'utilisateur accède à la page d'accueil YY
    Then la mise en page est correcte sur un écran de bureau
    Then la mise en page est correcte sur un écran tablette
    Then la mise en page est correcte sur un écran mobile
