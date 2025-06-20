import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user.service';
import { AuthService } from '../service/auth.service';
import { TestSuite } from '../model/TestSuite.model';
import { Exigences } from '../model/exigence.model';

@Component({
  selector: 'app-test-suite-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './test-suite-dashboard.component.html',
  styleUrl: './test-suite-dashboard.component.css'
})
export class TestSuiteDashboardComponent implements OnInit {
  testSuite: TestSuite | null = null;
  mission: Exigences | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';
  testSuiteId: number = 0;
  missionId: number = 0;
  userId: number = 0;
  testSuites: TestSuite[] = [];
  parsedJsonReport: any = null; // <-- Add this line

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    // Récupérer les paramètres depuis la route
    this.route.params.subscribe(params => {
      // Vérifier si c'est une route avec ID de test simple
      if (params['id']) {
        this.testSuiteId = +params['id']; // Le + convertit en number
        this.loadTestSuiteDetails();
      }
      // Vérifier si c'est une route avec mission et utilisateur
      else if (params['missionId'] && params['userId']) {
        this.missionId = +params['missionId'];
        this.userId = +params['userId'];
        this.loadTestsByMissionAndUser();
      }
      else {
        this.errorMessage = 'Paramètres de route invalides';
      }
    });
  }

  loadTestSuiteDetails(): void {
    // Vérifier l'authentification
    if (!this.authService.isloggedIn) {
      this.errorMessage = 'Vous devez être connecté pour voir les détails du test.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Pour l'instant, on récupère tous les tests et on filtre par ID
    // Plus tard, vous pourrez créer une API spécifique pour récupérer un test par ID
    this.userService.GetAllTests().subscribe({
      next: (data) => {
        const foundTest = data.find(test => test.id === this.testSuiteId);
        if (foundTest) {
          this.testSuite = foundTest;

          // 🔍 Debug: Afficher les détails du TestSuite
          console.log('📊 TestSuite trouvé:', foundTest);
          console.log('📝 Logs disponibles:', foundTest.logs ? 'OUI' : 'NON');
          console.log('📄 JSON Report disponible:', foundTest.jsonReport ? 'OUI' : 'NON');
          console.log('🔢 Métriques:', {
            totalTests: foundTest.totalTests,
            passedTests: foundTest.passedTests,
            failedTests: foundTest.failedTests,
            successRate: foundTest.successRate
          });

          // Parse JSON report if available
          if (foundTest.jsonReport) {
            try {
              this.parsedJsonReport = JSON.parse(foundTest.jsonReport);
              console.log('✅ JSON Report parsé avec succès:', this.parsedJsonReport);
            } catch (error) {
              console.error('❌ Erreur lors du parsing du JSON Report:', error);
              this.parsedJsonReport = null;
            }
          } else {
            console.warn('⚠️ Aucun JSON Report disponible');
            this.parsedJsonReport = null;
          }

          // Si le TestSuite a une mission associée, l'utiliser directement
          if (foundTest.mission) {
            this.mission = foundTest.mission;
            console.log('✅ Mission trouvée dans TestSuite:', foundTest.mission);
          }
        } else {
          this.errorMessage = `Test avec l'ID ${this.testSuiteId} non trouvé.`;
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement du test:', err);
        if (err.status === 403) {
          this.errorMessage = 'Accès refusé. Vérifiez vos permissions.';
        } else if (err.status === 401) {
          this.errorMessage = 'Session expirée. Veuillez vous reconnecter.';
        } else if (err.status === 0) {
          this.errorMessage = 'Impossible de contacter le serveur.';
        } else {
          this.errorMessage = `Erreur lors du chargement du test (${err.status}).`;
        }
        this.isLoading = false;
      }
    });
  }

  loadTestsByMissionAndUser(): void {
    // Vérifier l'authentification
    if (!this.authService.isloggedIn) {
      this.errorMessage = 'Vous devez être connecté pour voir les détails du test.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Récupérer les tests pour cette mission et cet utilisateur
    this.userService.getTestsByMissionAndUser(this.missionId, this.userId).subscribe({
      next: (tests) => {
        this.testSuites = tests;

        if (tests && tests.length > 0) {
          // Trier les tests par ID décroissant pour obtenir le plus récent
          // En cas d'égalité d'ID, trier par date d'exécution décroissante
          const sortedTests = tests.sort((a, b) => {
            if (b.id !== a.id) {
              return b.id - a.id;
            }
            // Si les IDs sont égaux, trier par date d'exécution
            return new Date(b.executionDate.toString()).getTime() - new Date(a.executionDate.toString()).getTime();
          });
          this.testSuite = sortedTests[0];

          console.log('🔍 Tests récupérés:', tests.map(t => ({ id: t.id, name: t.name, date: t.executionDate })));
          console.log('✅ TestSuite le plus récent sélectionné:', { id: this.testSuite.id, name: this.testSuite.name, date: this.testSuite.executionDate });

          // Parse JSON report if available
          if (this.testSuite.jsonReport) {
            try {
              this.parsedJsonReport = JSON.parse(this.testSuite.jsonReport);
            } catch {
              this.parsedJsonReport = null;
            }
          } else {
            this.parsedJsonReport = null;
          }

          // Si le TestSuite a une mission associée, l'utiliser directement
          if (this.testSuite.mission) {
            this.mission = this.testSuite.mission;
            console.log('✅ Mission trouvée dans TestSuite:', this.testSuite.mission);
          } else {
            // Sinon, récupérer les détails de la mission
            this.loadMissionDetails();
          }
        } else {
          this.errorMessage = `Aucun test trouvé pour la mission ${this.missionId} et l'utilisateur ${this.userId}.`;
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tests:', err);
        if (err.status === 403) {
          this.errorMessage = 'Accès refusé. Vérifiez vos permissions.';
        } else if (err.status === 401) {
          this.errorMessage = 'Session expirée. Veuillez vous reconnecter.';
        } else if (err.status === 0) {
          this.errorMessage = 'Impossible de contacter le serveur.';
        } else {
          this.errorMessage = `Erreur lors du chargement des tests (${err.status}).`;
        }
        this.isLoading = false;
      }
    });
  }

  loadMissionDetails(): void {
    // Récupérer les détails de la mission si nécessaire
    this.userService.listeTasks().subscribe({
      next: (missions) => {
        const foundMission = missions.find(m => m.id === this.missionId);
        if (foundMission) {
          this.mission = foundMission;
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement de la mission:', err);
      }
    });
  }

  goBack(): void {
    if (this.authService.isAdmin()) {
      this.router.navigate(['/tasks']);
    } else {
      this.router.navigate(['/allTasks']);
    }
  }

  getSuccessRateColor(): string {
    if (!this.testSuite) return 'text-secondary';

    const rate = this.testSuite.successRate;
    if (rate >= 90) return 'text-success';
    if (rate >= 70) return 'text-warning';
    return 'text-danger';
  }

  getSuccessRateIcon(): string {
    if (!this.testSuite) return 'fas fa-question-circle';

    const rate = this.testSuite.successRate;
    if (rate >= 90) return 'fas fa-check-circle';
    if (rate >= 70) return 'fas fa-exclamation-triangle';
    return 'fas fa-times-circle';
  }

  formatDuration(ms: number): string {
    if (ms < 1000) return `${ms} ms`;
    if (ms < 60000) return `${(ms / 1000).toFixed(1)} s`;
    return `${(ms / 60000).toFixed(1)} min`;
  }

  formatDate(dateString: string | String): string {
    try {
      const date = new Date(dateString.toString());
      return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return dateString.toString();
    }
  }
}
