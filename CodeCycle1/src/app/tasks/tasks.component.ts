import { Component, inject, OnInit } from '@angular/core';
import { Exigences } from '../model/exigence.model';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TestSuite } from '../model/TestSuite.model';


@Component({
  selector: 'app-tasks',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css'
})
export class TasksComponent implements OnInit{

  tasks: Exigences[] = [];
  users: User[] = [];
  allUsers: User[] = [];
  selectedTask: Exigences | null = null;
  selectedTaskForAssignment: Exigences | null = null;
  currentUser = new User();
  newMission = new Exigences();
  selectedUser: User | null = null;
  taskToUpdate: Exigences | null = null;
  isLoading: boolean = false;
  isLoadingUsers: boolean = false;
  isExecutingTests: boolean = false;
  testExecutionMessage: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  testSuite = new TestSuite();



  constructor(private userService: UserService, public authService: AuthService, private router: Router){

  }

  ngOnInit(): void {
    this.loadTasks();
  }

  /**
   * Méthode utilitaire pour afficher un message de succès temporaire
   */
  private showSuccessMessage(message: string, duration: number = 3000): void {
    this.successMessage = message;
    this.errorMessage = '';
    setTimeout(() => {
      this.successMessage = '';
    }, duration);
  }

  /**
   * Méthode utilitaire pour afficher un message d'erreur
   */
  private showErrorMessage(message: string): void {
    this.errorMessage = message;
    this.successMessage = '';
  }



  loadTasks() {
    this.errorMessage = '';
    this.isLoading = true;
    this.userService.listeTasks().subscribe({
      next: (exigences) => {
        this.tasks = exigences;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des tâches.';
        this.isLoading = false;
      }
    });

 }


 refreshMissions(): void {
    this.loadTasks();
  }



  deleteTask(task : Exigences){
    this.isLoading = true;
    let conf = confirm("Etes-vous sûr ?");
    if (conf){
      this.userService.deleteMission(task.id).subscribe({
        next: () => {
          this.loadTasks();
        }
      });
    }
  }

  // listeUsers(idMission : number){
  //   this.userService.getUserWithMissions(idMission).subscribe(usr =>{
  //     this.users = usr;
  //   })

  // }


  loadUsersWithTask(idMission: number){
    this.isLoading = true;
    this.errorMessage = '';

    this.userService.getUserWithMissions(idMission).subscribe( {
      next: (usr) => {
        this.users = usr;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs assignés à cette mission.';
        this.isLoading = false;
        this.users = []; 
      }
    } );
  }

  selectUsersMissions(task: Exigences){
    this.selectedTask = task ;
    this.loadUsersWithTask(this.selectedTask.id);
  }




  public selectUser(usr: User) {
    this.selectedUser = usr;
    if (this.selectedTask) {
      this.isLoading = true;
      this.errorMessage = '';

      this.userService.getTestsByMissionAndUser(this.selectedTask.id, usr.id).subscribe({
        next: (tests) => {
          this.isLoading = false;

          if (tests && tests.length > 0) {
            // Des tests existent, naviguer vers le dashboard
            this.router.navigate(['/test-suite-dashboard', 'mission', this.selectedTask!.id, 'user', usr.id]);
          } else {
            // Aucun test trouvé
            this.errorMessage = `Aucun test trouvé pour la mission "${this.selectedTask!.titre}" et l'utilisateur ${usr.firstName} ${usr.lastName}. Veuillez d'abord exécuter des tests.`;
          }
        },
        error: (err) => {
          this.isLoading = false;
          console.error('Erreur lors de la récupération des tests:', err);
        }
      });
    } else {
      this.errorMessage = 'Aucune tâche sélectionnée.';
    }
  }



  updateTask(task: Exigences){
    // Créer une copie de la tâche pour éviter la mutation directe de l'original
    this.taskToUpdate = { ...task };
    this.updateTaskFormGroup.patchValue(task);
    // Effacer les messages précédents
    this.errorMessage = '';
    this.successMessage = '';
  }

  saveUpdatedTask(){
    if (this.updateTaskFormGroup.invalid || !this.taskToUpdate) return;

    // Appliquer les modifications du formulaire à la tâche (copie déjà créée)
    Object.assign(this.taskToUpdate, this.updateTaskFormGroup.value);

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.userService.updateMission(this.taskToUpdate).subscribe({
      next: () => {
        this.isLoading = false;
        this.loadTasks();
        this.updateTaskFormGroup.reset();
        this.taskToUpdate = null;
        // Message de succès
        this.showSuccessMessage('Tâche mise à jour avec succès !', 3000);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.status === 404
          ? 'Tâche non trouvée'
          : 'Erreur lors de la mise à jour de la tâche.';
      }
    });
  }

  private _formBuilder = inject(FormBuilder);

  updateTaskFormGroup = this._formBuilder.group({
    idExigence: ['', Validators.required],
    titre: ['', Validators.required],
    description: ['', Validators.required],
    commentaire: ['']
  });

  // Méthodes pour l'assignation d'utilisateurs
  selectTaskForUserAssignment(task: Exigences) {
    this.selectedTaskForAssignment = task;
    this.loadAllUsers();
  }

  loadAllUsers() {
    this.isLoadingUsers = true;
    this.errorMessage = '';

    this.userService.listeUsers().subscribe({
      next: (users) => {
        this.allUsers = users;
        this.isLoadingUsers = false;
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs.';
        this.isLoadingUsers = false;
        this.allUsers = [];
      }
    });
  }

  assignUserToTask(user: User) {
    if (!this.selectedTaskForAssignment) {
      this.errorMessage = 'Aucune tâche sélectionnée pour l\'assignation.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.userService.addMissionToUser(user.id, this.selectedTaskForAssignment.idExigence).subscribe({
      next: () => {
        this.isLoading = false;

        // Rafraîchir la liste des tâches
        this.loadTasks();

        // Message de succès
        this.showSuccessMessage(`Mission "${this.selectedTaskForAssignment!.idExigence}" assignée avec succès à ${user.firstName} ${user.lastName} !`, 4000);

        // Reset des variables
        this.selectedTaskForAssignment = null;
      },
      error: (err) => {
        console.error('Erreur assignation:', err);
        this.errorMessage = err.status === 409
          ? `${user.firstName} ${user.lastName} est déjà assigné(e) à cette mission.`
          : `Erreur lors de l'assignation de la mission à ${user.firstName} ${user.lastName}.`;
        this.isLoading = false;
      }
    });
  }

  private lastTestSuiteIdBeforeExecution: number | null = null;

  /**
   * Exécute tous les tests Cucumber
   */
  executeAllTests() {
    this.isExecutingTests = true;
    this.isLoading = false; // Désactiver le loading général
    this.errorMessage = '';
    this.successMessage = '';
    this.testExecutionMessage = 'Préparation de l\'exécution des tests...';


    // D'abord, récupérer l'ID du dernier TestSuite existant avant l'exécution
    this.userService.getLatestGlobalTestSuite().subscribe({
      next: (existingTestSuite: TestSuite) => {
        this.lastTestSuiteIdBeforeExecution = existingTestSuite.id;
        this.testExecutionMessage = 'Lancement de l\'exécution des tests...';
        this.startTestExecution();
      },
      error: (err: any) => {
        if (err.status === 404) {
          // Aucun TestSuite existant, c'est normal pour la première exécution
          this.lastTestSuiteIdBeforeExecution = null;
          this.testExecutionMessage = 'Lancement de l\'exécution des tests (premier test)...';
          this.startTestExecution();
        } else {
          this.isExecutingTests = false;
          this.errorMessage = 'Erreur lors de la vérification des TestSuites existants.';
        }
      }
    });
  }

  /**
   * Lance l'exécution des tests après avoir récupéré l'état initial
   */
  private startTestExecution() {
    // Créer un TestSuite temporaire pour l'exécution globale
    const globalTestSuite = new TestSuite();
    globalTestSuite.name = 'Exécution globale de tous les tests';

    this.userService.executeAllTests(globalTestSuite).subscribe({
      next: () => {
        this.testExecutionMessage = 'Tests exécutés avec succès ! Génération du rapport en cours...';

        // Maintenant on attend que le NOUVEAU TestSuite soit créé
        this.waitForNewTestSuiteCreation();
      },
      error: (err: any) => {
        this.isExecutingTests = false;
        console.error('❌ Erreur lors de l\'exécution des tests:', err);

        if (err.status === 0) {
          this.errorMessage = 'Impossible de contacter le serveur backend.';
        } else if (err.status === 500) {
          this.errorMessage = 'Erreur interne du serveur lors de l\'exécution des tests.';
        } else {
          this.errorMessage = `Erreur lors de l\'exécution des tests (${err.status}). Veuillez réessayer.`;
        }
      }
    });
  }

  /**
   * Attend que le NOUVEAU TestSuite soit créé dans la base de données avant de rediriger
   */
  private waitForNewTestSuiteCreation() {
    
    this.testExecutionMessage = 'Attente de la finalisation du rapport de test...';

    const maxAttempts = 30; // Maximum 30 tentatives (30 secondes)
    let attempts = 0;

    const checkInterval = setInterval(() => {
      attempts++;
      this.testExecutionMessage = `Vérification du rapport de test... (tentative ${attempts}/${maxAttempts})`;

      this.userService.getLatestGlobalTestSuite().subscribe({
        next: (testSuite: TestSuite) => {

          // Vérifier si c'est un NOUVEAU TestSuite
          const isNewTestSuite = this.lastTestSuiteIdBeforeExecution === null ||
                                 testSuite.id > this.lastTestSuiteIdBeforeExecution;

          if (isNewTestSuite) {
            console.log('✅ NOUVEAU TestSuite détecté dans la base de données:', testSuite);
            clearInterval(checkInterval);
            this.isExecutingTests = false;
            this.testExecutionMessage = '';

            // Rediriger vers le dashboard avec le nouveau TestSuite
            this.router.navigate(['/test-suite-dashboard', testSuite.id]);
          } else {
            console.log('⏳ TestSuite trouvé mais c\'est l\'ancien (ID:', testSuite.id, '), on continue à attendre...');
          }
        },
        error: (err: any) => {
          if (attempts >= maxAttempts) {
            // Timeout atteint
            console.error('⏰ Timeout: Le nouveau TestSuite n\'a pas été trouvé après', maxAttempts, 'tentatives');
            clearInterval(checkInterval);
            this.isExecutingTests = false;
            this.testExecutionMessage = '';
            this.errorMessage = 'Les tests ont été exécutés mais le rapport n\'est pas encore disponible. Veuillez réessayer dans quelques instants.';
          } else if (err.status !== 404) {
            // Erreur autre que "non trouvé"
            console.error('❌ Erreur lors de la vérification du TestSuite:', err);
            clearInterval(checkInterval);
            this.isExecutingTests = false;
            this.testExecutionMessage = '';
            this.errorMessage = 'Erreur lors de la vérification du rapport de test. Veuillez réessayer.';
          }
          // Si c'est une erreur 404, on continue à attendre
        }
      });
    }, 1000); // Vérifier toutes les secondes
  }

  /**
   * Navigue vers le dashboard du dernier test global (sans tag spécifique)
   */
  viewGlobalTestDashboard() {
    this.isLoading = true;
    this.errorMessage = '';

    this.userService.getLatestGlobalTestSuite().subscribe({
      next: (testSuite: TestSuite) => {
        this.isLoading = false;
        // Naviguer vers le dashboard avec l'ID du TestSuite global
        this.router.navigate(['/test-suite-dashboard', testSuite.id]);
      },
      error: (err: any) => {
        this.isLoading = false;
        if (err.status === 404) {
          this.errorMessage = 'Aucun test global trouvé. Veuillez d\'abord exécuter tous les tests.';
        } else if (err.status === 0) {
          this.errorMessage = 'Impossible de contacter le serveur backend.';
        } else {
          this.errorMessage = `Erreur lors de la récupération du dernier test global (${err.status}).`;
        }
      }
    });
  }

  /**
   * Navigue vers le tableau de bord de test pour une mission donnée
   * @param missionId - L'ID de la mission
   */
  viewTestDashboard(missionId: number): void {
    // Marquer comme en cours de chargement (TOUS les boutons)
    this.isLoading = true;

    // Utiliser la nouvelle méthode pour récupérer le TestSuite le plus récent de cette mission
    this.userService.getLatestTestSuiteByMission(missionId).subscribe({
      next: (testSuite: TestSuite) => {

        // Naviguer vers le tableau de bord avec l'ID du TestSuite
        this.router.navigate(['/test-suite-dashboard', testSuite.id]);
        this.isLoading = false;
      }
    });
  }



}
