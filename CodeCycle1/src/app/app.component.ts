import { Component } from '@angular/core';
import { Router, RouterLink, RouterModule, RouterOutlet, NavigationEnd } from '@angular/router';
import { AuthService } from './service/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,RouterLink,ReactiveFormsModule,RouterModule,CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'CodeCycle1';
  isLoginPage = false;

   constructor (public authService: AuthService, private router: Router) {}

  ngOnInit () {
    this.authService.loadToken();
    if (this.authService.getToken()==null ||  this.authService.isTokenExpired())
      this.router.navigate(['/login']);
    

    // Écouter les changements de route pour détecter la page de login
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.isLoginPage = event.url === '/login';
    });

    // Vérifier la route initiale
    this.isLoginPage = this.router.url === '/login';
  }


  onLogout(){
    console.log("logout");
    this.authService.logout();
  }
}
