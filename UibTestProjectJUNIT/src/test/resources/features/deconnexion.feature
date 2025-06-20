@logout @security
Feature: Déconnexion utilisateur

  Scenario: Déconnexion réussie depuis l'interface utilisateur
  Given utilisateur connecté
  When deconnexion processus
  Then verification redirection