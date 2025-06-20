import { Component, OnInit, OnDestroy } from '@angular/core';
import { Exigences } from '../model/exigence.model';
import { User } from '../model/user.model';
import { UserService } from '../service/user.service';
import { AuthService } from '../service/auth.service';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-testing-workspace',
  imports: [CommonModule],
  templateUrl: './testing-workspace.component.html',
  styleUrl: './testing-workspace.component.css'
})
export class TestingWorkspaceComponent implements OnInit, OnDestroy {


    tasks!: Exigences[];
    users!: User[];
    selectedTask! : Exigences;
    currentUser! : string;
    Missions! : Exigences[];
    isLoadingMissions: boolean = false;
    private routerSubscription!: Subscription;

    constructor(
      private userService: UserService,
      public authService: AuthService,
      private router: Router
    ){

    }

    ngOnInit(): void {
      this.getMissionOfUSerLOggedIn();
      // Écouter les événements de navigation pour rafraîchir quand on revient sur cette page
      this.routerSubscription = this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe((event: NavigationEnd) => {
        // Si on navigue vers TestingWorkspace, rafraîchir les données
        if (event.url === '/TestingWorkspace') {
          this.getMissionOfUSerLOggedIn();
        }
      });
    }

    ngOnDestroy(): void {
      // Nettoyer la souscription pour éviter les fuites mémoire
      if (this.routerSubscription) {
        this.routerSubscription.unsubscribe();
      }
    }

    loadUsersWithTask(idMission: number){
    this.userService.getUserWithMissions(idMission).subscribe(usr=>{
      this.users = usr ;
  });
  }

    selectUsersMissions(task: Exigences){
    this.selectedTask = task ;
    console.log(this.selectedTask);
    this.loadUsersWithTask(this.selectedTask.id);
  }



  getMissionOfUSerLOggedIn() {
    this.currentUser = this.authService.loggedUser;
    this.isLoadingMissions = true;

    this.userService.getAllMissionOfUser(this.currentUser).subscribe({
      next: (missions) => {
        // Filtrer pour n'afficher que les missions non terminées
        this.Missions = missions.filter(mission => !(mission as any).completed);
        this.isLoadingMissions = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des missions:', error);
        this.Missions = [];
        this.isLoadingMissions = false;
      }
    });
  }

  /**
   * Démarre le processus de test pour une mission spécifique
   * @param mission La mission pour laquelle démarrer le test
   */
  startTestForMission(mission: Exigences) {
    // Stocker l'ID de la mission dans le localStorage pour le récupérer dans test-steps
    localStorage.setItem('currentMissionId', mission.id.toString());
    localStorage.setItem('currentMissionTitle', mission.titre );

    // Naviguer vers la page de test
    this.router.navigate(['/testSteps']);
  }

 

}
