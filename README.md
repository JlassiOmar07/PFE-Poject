# PFE-Project

Plateforme d’orchestration et d’automatisation de tests pour l’Union Internationale de Banques (UIB).

---

## Table des matières

1. [Aperçu](#aperçu)  
2. [Fonctionnalités](#fonctionnalités)  
3. [Architecture du projet](#architecture-du-projet)  
4. [Technologies utilisées](#technologies-utilisées)  
5. [Prérequis](#prérequis)  
6. [Installation](#installation)  
7. [Usage](#usage)  
   - [Démarrer l’interface (Front-end)](#démarrer-linterface-front-end)  
   - [Lancer les tests (Back-end)](#lancer-les-tests-back-end)  
8. [Structure du dépôt](#structure-du-dépôt)  
9. [Contribuer](#contribuer)  
10. [Licence](#licence)  

---

## Aperçu

Ce projet regroupe :

- **CodeCycle1 & CodeCycle11** : deux versions d’une interface web (Angular) pour lancer, suivre et visualiser des séries de tests automatisés.  
- **UibTestProjectJUNIT** : ensemble de tests automatisés (JUnit 5, Cucumber) destinés à valider les fonctionnalités métier de la plateforme bancaire UIB.

---

## Fonctionnalités

- Orchestration et exécution de suites JUnit et scénarios BDD (Cucumber).  
- Interface web : lancement de campagnes de tests, suivi en temps réel, visualisation des rapports.  
- Rapports HTML détaillés pour chaque série de tests.  
- Intégration CI/CD via GitHub Actions ou Jenkins.  

---

## Architecture du projet

```mermaid
graph LR
  subgraph Front-end
    UI[Angular (CodeCycle1 & CodeCycle11)]
  end

  subgraph API & Tests
    BE[UibTestProjectJUNIT<br/>(Java, JUnit, Cucumber)]
    DB[(MySQL)]
    Reports[Rapports HTML]
  end

  UI -->|REST API JSON| BE
  BE --> DB
  BE --> Reports
