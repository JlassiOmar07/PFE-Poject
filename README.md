# PFE-Project

Plateforme complète d'orchestration et d'automatisation de tests pour l’Union Internationale de Banques (UIB).

---

## Table des matières

1. [Aperçu](#aperçu)
2. [Modules](#modules)

   * [Front-end (CodeCycle1)](#front-end-codecycle1)
   * [Back-end (CodeCycle)](#back-end-codecycle)
   * [Suite de tests (UibTestProjectJUNIT)](#suite-de-tests-uibtestprojectjunit)
3. [Technologies utilisées](#technologies-utilisées)
4. [Prérequis](#prérequis)
5. [Installation](#installation)
6. [Usage](#usage)

   * [Démarrer le Front-end](#démarrer-le-front-end)
   * [Lancer l'API et le Back-end](#lancer-lapi-et-le-back-end)
   * [Exécution des tests automatisés](#exécution-des-tests-automatisés)
7. [Structure du dépôt](#structure-du-dépôt)
8. [Contribuer](#contribuer)
9. [Licence](#licence)

---

## Aperçu

Ce projet PFE-Project offre une solution complète pour :

* Lancement et suivi d’exécutions de tests automatisés via une interface web.
* Exposition d’une API REST pour la gestion des utilisateurs, rôles et missions.
* Exécution et reporting de tests unitaires et fonctionnels (BDD).

---

## Modules

### Front-end (CodeCycle1)

Application Angular pour la gestion des campagnes de tests :

* Sélection de projets et scénarios Cucumber.
* Configuration et exécution de tests.
* Gestion des utilisateurs et missions.
* Visualisation des rapports en temps réel.
* Interface responsive basée sur Angular Material & Bootstrap.

### Back-end (CodeCycle)

API Spring Boot RESTful pour le pilotage de la plateforme :

* Inscription et authentification JWT.
* Contrôle d’accès par rôles (ADMIN, USER).
* CRUD pour utilisateurs, rôles et missions.
* Assignation de rôles et de missions.
* Recherche d’utilisateurs par rôle, nom ou mission.
* Support CORS pour intégration Front-end.

### Suite de tests (UibTestProjectJUNIT)

Projet Maven de tests automatisés :

* Tests unitaires JUnit 5.
* Scénarios BDD avec Cucumber.
* Modèle Page Object pour la maintenabilité.
* Génération de rapports HTML détaillés.

---

## Technologies utilisées

* **Front-end** : Angular 19, TypeScript, Angular Material, Bootstrap
* **Back-end** : Java 17+, Spring Boot, Spring Security (JWT), JPA/Hibernate, Maven
* **Tests** : Java 11+, Maven, Selenium WebDriver, Cucumber, JUnit 5, ChromeDriver
* **Base de données** : MySQL (configurable)
* **CI/CD** : GitHub Actions ou Jenkins

---

## Prérequis

* Node.js >= 18, Angular CLI (`@angular/cli`)
* Java 17 ou supérieur
* Maven 3.6+
* MySQL 5.7+ (ou H2 pour tests)
* Chrome et ChromeDriver dans le PATH pour Selenium

---

## Installation

1. **Cloner le dépôt**

   ```bash
   git clone https://github.com/JlassiOmar07/PFE-Poject.git
   cd PFE-Poject
   ```

2. **Installer le Front-end**

   ```bash
   cd CodeCycle1
   npm install
   ```

3. **Configurer la base de données**

   * Modifier `src/main/resources/application.properties` dans le module `CodeCycle` pour vos identifiants MySQL.

4. **Compiler et lancer le Back-end**

   ```bash
   cd ../CodeCycle
   mvn clean install
   mvn spring-boot:run
   ```

5. **Démarrer le Front-end**

   ```bash
   cd ../CodeCycle1
   npm start
   ```

   L’application sera accessible sur `http://localhost:4200`.

---

## Usage

### Démarrer le Front-end

Ouvrez `http://localhost:4200` dans votre navigateur après `npm start`.

### Lancer l'API et le Back-end

L’API REST tourne par défaut sur `http://localhost:8080`.

### Exécution des tests automatisés

Dans le module `UibTestProjectJUNIT` :

```bash
cd ../UibTestProjectJUNIT
mvn clean test
```

* Rapports JUnit : `target/surefire-reports`
* Rapports Cucumber : `target/cucumber-reports`

---

## Structure du dépôt

```
PFE-Poject/
├─ CodeCycle1/             # Front-end Angular
├─ CodeCycle/              # API Spring Boot
├─ UibTestProjectJUNIT/    # Suite de tests Maven (JUnit, Cucumber)
└─ README.md               # Document principal (celui-ci)
```

---

## Contribuer

1. Forkez ce dépôt.
2. Créez une branche de fonctionnalité :

   ```bash
   ```

git checkout -b feature/ma-fonctionnalite

````
3. Commitez vos changements :
   ```bash
 git commit -m "Ajoute : description de la fonctionnalité"
````

4. Pushez :

   ```bash
   ```

git push origin feature/ma-fonctionnalite

```
5. Ouvrez une Pull Request sur GitHub.

Merci pour vos contributions !

---

## Licence

Ce projet est privé et n'est pas destiné à un usage public.

```
