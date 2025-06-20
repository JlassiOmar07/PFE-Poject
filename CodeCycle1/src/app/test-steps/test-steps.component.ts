import { Component, ViewChild, ElementRef, AfterViewInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { MatStepperModule }    from '@angular/material/stepper';
import { MatIconModule }       from '@angular/material/icon';
import { MatButtonModule }     from '@angular/material/button';
import { MatFormFieldModule }  from '@angular/material/form-field';
import { MatInputModule }      from '@angular/material/input';
import { MatSelectModule }     from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule }      from '@angular/material/chips';
import { MatDividerModule }    from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UserService } from '../service/user.service';
import { AuthService } from '../service/auth.service';
import { MatCardModule } from '@angular/material/card';
import { TestSuite } from '../model/TestSuite.model';
import { Router } from '@angular/router';

interface Step {
  label: string;
  icon: string;
  content?: string;
  inputPlaceholder?: string;
}

@Component({
  selector: 'app-test-steps',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressBarModule,
    MatChipsModule,
    MatDividerModule,
    MatSnackBarModule,
    MatCardModule
  ],
  templateUrl: './test-steps.component.html',
  styleUrl: './test-steps.component.css'
})
export class TestStepsComponent implements AfterViewInit, OnDestroy {
  // Référence au stepper pour la navigation programmatique
  @ViewChild('stepper', { static: false }) stepper!: MatStepper;

  // FormBuilder et FormGroups pour la validation des étapes
  private _formBuilder = inject(FormBuilder);

  // FormGroups pour chaque étape
  firstFormGroup!: FormGroup;   // Étape 1: Sélection du projet
  secondFormGroup!: FormGroup;  // Étape 2: Configuration du test
  thirdFormGroup!: FormGroup;   // Étape 3: Exécution (pas de validation)

  // Chemin du projet par défaut
  defaultProjectPath: string = "C:\\workspace\\testautomation\\webDriver_java\\UibTestProjectJUNIT";

  // Projet sélectionné
  selectedProject: string | null = null;

  // Liste des projets disponibles
  availableProjects: {name: string, path: string}[] = [
    {
      name: "UibTestProjectJUNIT",
      path: "C:\\workspace\\testautomation\\webDriver_java\\UibTestProjectJUNIT"
    }
  ];


  testSuite = new TestSuite();

  // ID de la mission en cours
  currentMissionId: number | null = null;
  currentMissionTitle: string | null = null;

  steps: Step[] = [
    {
      label: 'ÉTAPE 1',
      icon: 'search',
      content: 'Choisir un projet',
      inputPlaceholder: 'Sélectionner un projet',
    },
    {
      label: 'ÉTAPE 2',
      icon: 'label',
      content: 'Configuration du test Cucumber',
      inputPlaceholder: 'Saisir le tag et le nom du test',
    },
    {
      label: 'ÉTAPE 3',
      icon: 'description',
      content: 'Exécution et visualisation du rapport',
    },
  ];

  // Indicateur de chargement
  isLoading: boolean = false;

  // Propriétés pour l'exécution et les logs en temps réel
  isExecuting: boolean = false;
  executionLogs: string = '';
  executionStatus: string = 'En attente';
  private logPollingInterval: any;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit() {
    // Initialiser les FormGroups pour la validation des étapes
    this.initializeFormGroups();

    // Récupérer l'ID utilisateur depuis l'authentification
    this.getUserIdFromAuth();

    // Récupérer l'ID de la mission depuis le localStorage
    const missionId = localStorage.getItem('currentMissionId');
    const missionTitle = localStorage.getItem('currentMissionTitle');

    if (missionId) {
      this.currentMissionId = parseInt(missionId, 10);
      this.currentMissionTitle = missionTitle;
    } else {
      console.log('Aucune mission trouvée dans localStorage');
    }
  }

  /**
   * Initialise les FormGroups pour la validation des étapes
   */
  private initializeFormGroups(): void {
    // Étape 1: Validation de la sélection du projet
    this.firstFormGroup = this._formBuilder.group({
      selectedProject: ['', Validators.required]
    });

    // Étape 2: Validation de la configuration du test
    this.secondFormGroup = this._formBuilder.group({
      tag: ['', Validators.required],
      name: ['', Validators.required]
    });

    // Étape 3: Pas de validation nécessaire pour l'exécution
    this.thirdFormGroup = this._formBuilder.group({
      // Groupe vide, juste pour la cohérence
    });
  }

  /**
   * Méthode appelée lorsqu'un projet est sélectionné dans le dropdown
   * @param projectName Nom du projet sélectionné
   */
  onProjectSelected(projectName: string) {
    this.selectedProject = projectName;

    // Mettre à jour le FormGroup de l'étape 1
    this.firstFormGroup.patchValue({
      selectedProject: projectName
    });

    // Mettre à jour le chemin du projet
    const selectedProjectObj = this.availableProjects.find(p => p.name === projectName);
    if (selectedProjectObj) {
      this.defaultProjectPath = selectedProjectObj.path;
    }
  }

  /**
   * Ouvre l'IDE avec le projet sélectionné en utilisant l'API backend
   * Cette méthode envoie une requête au serveur pour lancer l'IDE
   */
  openIdeWithProject() {
    // Utiliser le service pour lancer l'IDE via l'API
    this.userService.launchIDE().subscribe({
      next: (response) => {
        // Désactiver l'indicateur de chargement
        this.isLoading = false;
        this.snackBar.open("IDE en train d'ouvrir...", 'Fermer', {
          duration: 8000,
          panelClass: ['error-snackbar']
        });

      }
    });
  }


  /**
   * Méthode du cycle de vie appelée après l'initialisation de la vue
   */
  ngAfterViewInit() {
    console.log(this.stepper);
  }

  // =====  MÉTHODES POUR LA CONFIGURATION DES TESTS =====

  /**
   * Récupère l'ID utilisateur depuis le service d'authentification
   */
  getUserIdFromAuth() {
    // Récupérer l'utilisateur connecté depuis AuthService
    const loggedUser = this.authService.loggedUser;
    if (loggedUser) {
      // Si vous avez un service pour récupérer l'ID par email
      this.userService.listeUsers().subscribe(users => {
        const currentUser = users.find(user => user.email === loggedUser);
        if (currentUser) {
          this.testSuite.user = currentUser;
        }
      });
    }
  }

  /**
   * Retourne le FormGroup approprié pour chaque étape
   * @param stepIndex Index de l'étape
   */
  getStepControl(stepIndex: number): FormGroup {
    switch (stepIndex) {
      case 0:
        return this.firstFormGroup;
      case 1:
        return this.secondFormGroup;
      case 2:
        return this.thirdFormGroup;
      default:
        return this.firstFormGroup;
    }
  }

  /**
   * Retourne le message d'erreur approprié pour chaque étape
   * @param stepIndex Index de l'étape
   */
  getStepErrorMessage(stepIndex: number): string {
    switch (stepIndex) {
      case 0:
        return 'Veuillez sélectionner un projet.';
      case 1:
        return 'Veuillez remplir le tag Cucumber et le nom du test.';
      case 2:
        return '';
      default:
        return '';
    }
  }

  /**
   * Vérifie si une étape est complétée
   * @param stepIndex Index de l'étape
   */
  isStepCompleted(stepIndex: number): boolean {
    const formGroup = this.getStepControl(stepIndex);
    const isValid = formGroup ? formGroup.valid : false;
    console.log(`Étape ${stepIndex} - FormGroup valide:`, isValid, 'FormGroup:', formGroup?.value);
    return isValid;
  }

  /**
   * Synchronise les données du testSuite avec le FormGroup de l'étape 2
   */
  onTestSuiteDataChange(): void {
    // Mettre à jour le FormGroup avec les données actuelles
    this.secondFormGroup.patchValue({
      tag: this.testSuite.tag || '',
      name: this.testSuite.name || ''
    }, { emitEvent: false }); // emitEvent: false pour éviter les boucles infinies
  }

  /**
   * Valide la configuration du test
   */
  validateTestSuite(): boolean {
    // Synchroniser d'abord les données avec le FormGroup
    this.onTestSuiteDataChange();

    if (!this.testSuite.tag) {
      this.snackBar.open('Veuillez saisir un tag Cucumber', 'Fermer', {
        duration: 3000,
        panelClass: ['warning-snackbar']
      });
      return false;
    }

    if (!this.testSuite.name) {
      this.snackBar.open('Veuillez saisir un nom pour le test', 'Fermer', {
        duration: 3000,
        panelClass: ['warning-snackbar']
      });
      return false;
    }

    // Valider le format du tag (doit commencer par @)
    if (!this.testSuite.tag.startsWith('@')) {
      this.testSuite.tag = '@' + this.testSuite.tag;
    }
    return true;
  }

  private lastTestSuiteIdBeforeExecution: number | null = null;

  /**
   * Exécute les tests avec la configuration actuelle
   */
  executeTests() {
    if (!this.validateTestSuite()) {
      return;
      // ajouter si false afficher un erreur : Tag Cucumber présent et non vide / Nom du test présent et non vide
    }

    // Démarrer l'exécution
    this.isExecuting = true;
    this.executionStatus = 'En cours d\'exécution...';
    this.executionLogs = 'Démarrage de l\'exécution des tests...\n';

    // D'abord, récupérer l'ID du dernier TestSuite existant pour cette mission avant l'exécution
    if (this.currentMissionId) {
      this.userService.getLatestTestSuiteByMission(this.currentMissionId).subscribe({
        next: (existingTestSuite: TestSuite) => {
          this.lastTestSuiteIdBeforeExecution = existingTestSuite.id;
          console.log('📋 Dernier TestSuite de la mission avant exécution - ID:', this.lastTestSuiteIdBeforeExecution);
          this.startTestExecution();
        },
        error: (err: any) => {
          if (err.status === 404) {
            // Aucun TestSuite existant pour cette mission, c'est normal pour la première exécution
            this.lastTestSuiteIdBeforeExecution = null;
            console.log('📋 Aucun TestSuite existant pour cette mission avant exécution');
            this.startTestExecution();
          } else {
            this.isExecuting = false;
            this.executionStatus = 'Erreur d\'exécution';
            this.executionLogs += 'Erreur lors de la vérification des TestSuites existants.\n';
            this.snackBar.open('Erreur lors de la vérification des tests existants', 'Fermer', {
              duration: 5000,
              panelClass: ['error-snackbar']
            });
          }
        }
      });
    } else {
      // Pas d'ID de mission, exécution directe
      this.lastTestSuiteIdBeforeExecution = null;
      this.startTestExecution();
    }
  }

  /**
   * Lance l'exécution des tests après avoir récupéré l'état initial
   */
  private startTestExecution() {
    // Utiliser le service pour lancer l'exécution via l'API avec l'ID de mission
    this.userService.executeTests(this.testSuite, this.currentMissionId || undefined).subscribe({
      next: (response) => {
        this.executionLogs += 'Tests lancés avec succès !\n';
        this.executionLogs += response + '\n';
        this.executionStatus = 'Exécution terminée';

        this.snackBar.open('Tests lancés avec succès !', 'Fermer', {
          duration: 5000,
          panelClass: ['success-snackbar']
        });

        // Maintenant on attend que le NOUVEAU TestSuite soit créé avant de marquer la mission comme terminée
        if (this.currentMissionId) {
          this.waitForNewTestSuiteCreation();
        } else {
          this.isExecuting = false;
        }
      },
      error: (error) => {
        this.executionLogs += 'Erreur lors de l\'exécution des tests:\n';
        this.executionLogs += error.message + '\n';
        this.executionStatus = 'Erreur d\'exécution';
        this.isExecuting = false;

        this.snackBar.open('Erreur lors de l\'exécution des tests', 'Fermer', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }


  

  /**
   * Attend que le NOUVEAU TestSuite soit créé dans la base de données avant de finaliser
   */
  private waitForNewTestSuiteCreation() {
    console.log('⏳ Vérification de la création du NOUVEAU TestSuite pour la mission...');
    console.log('📋 ID du dernier TestSuite avant exécution:', this.lastTestSuiteIdBeforeExecution);

    const maxAttempts = 3000; // Maximum 30 tentatives (30 secondes)
    let attempts = 0;

    const checkInterval = setInterval(() => {
      attempts++;
      console.log(`🔍 Tentative ${attempts}/${maxAttempts} - Vérification du nouveau TestSuite...`);

      if (this.currentMissionId) {
        this.userService.getLatestTestSuiteByMission(this.currentMissionId).subscribe({
          next: (testSuite: TestSuite) => {
            console.log('📊 TestSuite trouvé - ID:', testSuite.id, 'vs ID précédent:', this.lastTestSuiteIdBeforeExecution);

            // Vérifier si c'est un NOUVEAU TestSuite
            const isNewTestSuite = this.lastTestSuiteIdBeforeExecution === null ||
                                   testSuite.id > this.lastTestSuiteIdBeforeExecution;

            if (isNewTestSuite) {
              console.log('✅ NOUVEAU TestSuite détecté pour la mission:', testSuite);
              clearInterval(checkInterval);
              this.isExecuting = false;

              // Marquer la mission comme terminée et rediriger vers le dashboard
              this.markMissionAsCompletedAndRedirect(testSuite.id);
            } else {
              console.log('⏳ TestSuite trouvé mais c\'est l\'ancien (ID:', testSuite.id, '), on continue à attendre...');
            }
          },
          error: (err: any) => {
            if (attempts >= maxAttempts) {
              // Timeout atteint
              console.error('⏰ Timeout: Le nouveau TestSuite n\'a pas été trouvé après', maxAttempts, 'tentatives');
              clearInterval(checkInterval);
              this.isExecuting = false;
              this.executionLogs += 'Les tests ont été exécutés mais le rapport n\'est pas encore disponible.\n';
            } else if (err.status !== 404) {
              // Erreur autre que "non trouvé"
              console.error('❌ Erreur lors de la vérification du TestSuite:', err);
              clearInterval(checkInterval);
              this.isExecuting = false;
              this.executionLogs += 'Erreur lors de la vérification du rapport de test.\n';
            }
            // Si c'est une erreur 404, on continue à attendre
          }
        });
      }
    }, 1000); // Vérifier toutes les secondes
  }

  /**
   * Marque la mission actuelle comme terminée et redirige vers le dashboard
   */
  private markMissionAsCompletedAndRedirect(testSuiteId: number) {
    if (!this.currentMissionId) {
      console.log('Pas d\'ID de mission, abandon');
      return;
    }

    // Utiliser la vraie méthode markMissionAsCompleted du service
    this.userService.markMissionAsCompleted(this.currentMissionId).subscribe({
      next: () => {
        // Nettoyer le localStorage
        localStorage.removeItem('currentMissionId');
        localStorage.removeItem('currentMissionTitle');

        // Afficher un message de succès
        this.snackBar.open(
          `Mission "${this.currentMissionTitle}" terminée avec succès !`,
          'Fermer',
          {
            duration: 4000,
            panelClass: ['success-snackbar']
          }
        );

        // Rediriger vers le dashboard du nouveau TestSuite après un court délai
        setTimeout(() => {
          this.router.navigate(['/test-suite-dashboard', testSuiteId]);
        }, 1500);
      },
      error: (err) => {
        console.error('Erreur lors de la finalisation de la mission:', err);
        // Même en cas d'erreur de finalisation, on peut rediriger vers le dashboard
        setTimeout(() => {
          this.router.navigate(['/test-suite-dashboard', testSuiteId]);
        }, 1500);
      }
    });
  }

  /**
   * Retourne l'icône appropriée selon le statut d'exécution
   */
  getExecutionStatusIcon(): string {
    if (this.isExecuting) {
      return 'sync';
    } else if (this.executionStatus === 'Exécution terminée') {
      return 'check_circle';
    } else if (this.executionStatus === 'Erreur d\'exécution') {
      return 'error';
    } else {
      return 'schedule';
    }
  }

  /**
   * Nettoie les ressources lors de la destruction du composant
   */
  ngOnDestroy() {
    if (this.logPollingInterval) {
      clearInterval(this.logPollingInterval);
    }
  }

}

