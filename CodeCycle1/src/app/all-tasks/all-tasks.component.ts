import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { AuthService } from '../service/auth.service';
import { Exigences } from '../model/exigence.model';
import { TestSuite } from '../model/TestSuite.model';

@Component({
  selector: 'app-all-tasks',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './all-tasks.component.html',
  styleUrl: './all-tasks.component.css'
})
export class AllTasksComponent implements OnInit {
  completedMissions: Exigences[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  currentUser: string = '';



  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCompletedMissions();
  }

  loadCompletedMissions() {

    this.isLoading = true;
    this.errorMessage = '';
    this.currentUser = this.authService.loggedUser;

    this.userService.getAllMissionOfUser(this.currentUser).subscribe({
      next: (missions) => {

        // Filtrer pour ne garder que les missions terminées
        this.completedMissions = missions.filter(mission => (mission as any).completed === true);


        this.isLoading = false;
      }
    });
  }



  /**
   * Rafraîchit la liste des missions terminées
   */
  refreshMissions(): void {
    this.loadCompletedMissions();
  }

  /**
   * Navigue vers l'espace de test
   */
  goToTestingWorkspace(): void {
    this.router.navigate(['/TestingWorkspace']);
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
