import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../service/user.service';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { Exigences } from '../model/exigence.model';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';


// Interface pour les informations consolidées de missions
interface UserMissionInfo {
  allMissions: Exigences[];
  activeMissions: Exigences[];
  completedMissions: Exigences[];
  hasActiveMissions: boolean;
  hasAnyMissions: boolean;
  activeMissionsCount: number;
  title: string;
}

@Component({
  selector: 'app-users',
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})

export class UsersComponent implements OnInit {
  @ViewChild('stepper') stepper!: MatStepper;

  users: User[] = [];
  selectedUser: User | null = null;
  userMissionsMap: Map<number, Exigences[]> = new Map();
  isLoading: boolean = true;
  errorMessage: string = '';

  // États pour le modal de mission
  isSubmittingMission: boolean = false;
  missionErrorMessage: string = '';

  // FormGroups pour le stepper
  step1FormGroup!: FormGroup;
  step2FormGroup!: FormGroup;
  step3FormGroup!: FormGroup;
  step4FormGroup!: FormGroup;

  constructor(
    private userService: UserService,
    public authService: AuthService,
    private formBuilder: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.initializeForms();
  }

  ngOnInit(): void {
    this.chargerUsers();
  }


  /**
   * Initialise les FormGroups pour le stepper
   */
  initializeForms(): void {
    this.step1FormGroup = this.formBuilder.group({
      idExigence: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.step2FormGroup = this.formBuilder.group({
      titre: ['', [Validators.required, Validators.minLength(5)]]
    });

    this.step3FormGroup = this.formBuilder.group({
      description: ['', [Validators.required, Validators.minLength(10)]]
    });

    this.step4FormGroup = this.formBuilder.group({
      commentaire: ['']
    });
  }

  selectUserForMission(usr: User) {
    this.selectedUser = usr;
    this.loadMissionsForUser(usr);
    this.resetMissionForm();
  }

  /**
   * Charge les missions d'un utilisateur spécifique et met à jour le cache
   */
  loadMissionsForUser(user: User) {
    this.userService.getAllMissionOfUser(user.email).subscribe({
      next: (missions) => {
        this.userMissionsMap.set(user.id, missions);
      },
      error: (err) => {
        console.error(`❌ Erreur lors du chargement des missions pour ${user.firstName} ${user.lastName}:`, err);
      }
    });
  }

  
  getUserMissionInfo(userId: number): UserMissionInfo {
    const allMissions = this.userMissionsMap.get(userId) || [];
    const activeMissions = allMissions.filter(m => !m.completed);
    const completedMissions = allMissions.filter(m => m.completed);

    return {
      allMissions,
      activeMissions,
      completedMissions,
      hasActiveMissions: activeMissions.length > 0,
      hasAnyMissions: allMissions.length > 0,
      activeMissionsCount: activeMissions.length,
      title: activeMissions.length > 0
        ? `L'utilisateur a ${activeMissions.length} mission(s) active(s)`
        : 'Attribuer une nouvelle mission'
    };
  }

  /**
   * Charge tous les utilisateurs et leurs missions associées
   */
  chargerUsers(){
    this.isLoading = true;
    this.errorMessage = '';

    this.userService.listeUsers().subscribe({
      next: (usr) => {
        this.users = usr;
        this.isLoading = false;

        // Charger les missions pour chaque utilisateur
        this.loadAllUsersMissions(usr);
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs.';
        this.isLoading = false;
      }
    });
  }

  /**
   * Charge les missions pour tous les utilisateurs
   */
  private loadAllUsersMissions(users: User[]) {
    users.forEach(user => {
      this.loadMissionsForUser(user);
    });
  }

  deleteUser(usr: User){
    if (confirm(`Êtes-vous sûr de vouloir supprimer ${usr.firstName} ${usr.lastName} ?`)) {
      this.userService.deleteUser(usr.id).subscribe({
        next: () => this.chargerUsers(),
        error: () => {
          this.errorMessage = 'Erreur lors de la suppression de l\'utilisateur';
        }
      });
    }
  }

  
  /**
   * Valide tous les formulaires avant soumission
   */
  public validateAllForms(): boolean {
    return this.step1FormGroup.valid &&
           this.step2FormGroup.valid &&
           this.step3FormGroup.valid;
  }

  /**
   * Ajoute une nouvelle mission et l'assigne à l'utilisateur sélectionné
   */
  ajouterMission(): void {
    // Réinitialiser les messages d'erreur
    this.missionErrorMessage = '';

    if (!this.selectedUser) {
      this.missionErrorMessage = 'Aucun utilisateur sélectionné';
      return;
    }

    // Validation des formulaires
    if (!this.validateAllForms()) {
      this.missionErrorMessage = 'Veuillez remplir tous les champs requis correctement';
      return;
    }

    // Construire l'objet mission à partir des formulaires
    const missionData = new Exigences();
    missionData.idExigence = this.step1FormGroup.get('idExigence')?.value;
    missionData.titre = this.step2FormGroup.get('titre')?.value;
    missionData.description = this.step3FormGroup.get('description')?.value;
    missionData.commentaire = this.step4FormGroup.get('commentaire')?.value || '';

    const selectedUser = this.selectedUser; // Capture locale pour éviter les problèmes de null
    this.isSubmittingMission = true;

    this.userService.createExigence(missionData).subscribe({
      next: (mission: Exigences) => {
        this.userService.addMissionToUser(selectedUser.id, mission.idExigence).subscribe({
          next: () => {
            this.isSubmittingMission = false;
            this.loadMissionsForUser(selectedUser);
            this.resetMissionForm();

            // Afficher le snackbar de succès AVANT la navigation
            this.snackBar.open(
              `Mission "${mission.titre}" assignée avec succès à ${selectedUser.firstName} ${selectedUser.lastName}`,
              'Fermer',
              {
                duration: 5000,
                panelClass: ['success-snackbar']
              }
            );

            // Fermer le modal et naviguer vers le composant tasks
            this.closeModalAndNavigate();
          },
          error: (err) => {
            this.isSubmittingMission = false;
            console.error('❌ Erreur lors de l\'attribution de la mission:', err);
            this.missionErrorMessage = 'Erreur lors de l\'attribution de la mission à l\'utilisateur';
          }
        });
      },
      error: (err) => {
        this.isSubmittingMission = false;
        console.error('❌ Erreur lors de la création de la mission:', err);
        this.missionErrorMessage = 'Erreur lors de la création de la mission';
      }
    });
  }

  /**
   * Ferme le modal et navigue vers le composant tasks
   */
  private closeModalAndNavigate(): void {
    // Fermer le modal Bootstrap
    const modalElement = document.getElementById('missionModal');
    if (modalElement) {
      const modal = (window as any).bootstrap?.Modal?.getInstance(modalElement);
      if (modal) {
        modal.hide();
      }
    }

    // Naviguer après un court délai pour s'assurer que le modal est fermé
    setTimeout(() => {
      this.router.navigate(['/tasks']);
    }, 200);
  }



  /**
   * Remet à zéro le formulaire de mission
   */
  public resetMissionForm(): void {
    this.missionErrorMessage = '';
    this.isSubmittingMission = false;

    // Réinitialiser les FormGroups
    this.step1FormGroup.reset();
    this.step2FormGroup.reset();
    this.step3FormGroup.reset();
    this.step4FormGroup.reset();

    // Réinitialiser le stepper si disponible
      this.stepper.reset();
    
  }


}
