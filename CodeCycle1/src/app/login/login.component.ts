import { Component } from '@angular/core';
import { User } from '../model/user.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  user = new User();
  err : number = 0 ;
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private authService : AuthService,
    private router: Router) { }

  onLoggedin() {
    // Réinitialiser les erreurs et activer le loading
    this.err = 0;
    this.errorMessage = '';
    this.isLoading = true;

    // Validation côté client
    if (!this.user.email || !this.user.password) {
      this.err = 1;
      this.errorMessage = 'Veuillez remplir tous les champs.';
      this.isLoading = false;
      return;
    }

    // Validation format email
    // const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // if (!emailRegex.test(this.user.email)) {
    //   this.err = 1;
    //   this.errorMessage = 'Veuillez entrer un email valide.';
    //   this.isLoading = false;
    //   return;
    // }

    this.authService.login(this.user).subscribe({
      next: (data) => {
        let jwToken = data.headers.get('Authorization')!;
        this.authService.saveToken(jwToken);
        this.isLoading = false;
        this.router.navigate(['/acceuil']);
      },
      error: (err: any) => {
        this.isLoading = false;
        this.err = 1;
         this.errorMessage = 'Email ou mot de passe incorrect.';
      }
    });
  }

}
