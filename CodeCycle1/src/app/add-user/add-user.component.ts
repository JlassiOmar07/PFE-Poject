import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../model/user.model';
import { UserService } from '../service/user.service';
import { Role } from '../model/role.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-add-user',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit{

  userForm!: FormGroup;
  roles!: Role[];
  newRoleName!: string;
  isSubmitting = false;
  errorMessage = '';

 ngOnInit(): void {
     this.initializeForm();
     this.userService.listeRoles().subscribe(r => {
      this.roles = r ;
     })
  }

  private initializeForm(): void {
    this.userForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      roleName: ['', Validators.required]
    });
  }


  async addUser() {
    if (this.userForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    
      // Vérifier d'abord si l'email existe déjà
      const emailExists = await firstValueFrom(this.userService.checkEmailExists(this.userForm.value.email));
      if (emailExists) {
        this.errorMessage = `Un utilisateur avec l'email '${this.userForm.value.email}' existe déjà`;
        this.isSubmitting = false;
        return;
      }

      // Créer l'objet utilisateur à partir du formulaire
      const newUser = new User();
      newUser.firstName = this.userForm.value.firstName;
      newUser.lastName = this.userForm.value.lastName;
      newUser.email = this.userForm.value.email;
      newUser.password = this.userForm.value.password;

      // 1) Créer l'utilisateur
      this.userService.ajouterUser(newUser).subscribe({
        next: (savedUser) => {
          // 2) Ajouter le rôle
          this.userService.addRoleToUser(savedUser.email, this.userForm.value.roleName).subscribe({
            next: () => {
              // 3) Navigation vers la liste des utilisateurs
              this.router.navigate(['users']);
            },
            error: () => {
              this.errorMessage = 'Utilisateur créé mais erreur lors de l\'ajout du rôle';
              this.isSubmitting = false;
            }
          });
        }
      });
    
  }

  private markFormGroupTouched(): void {
    Object.keys(this.userForm.controls).forEach(key => {
      this.userForm.get(key)?.markAsTouched();
    });
  }

  // Méthodes utilitaires pour la validation
  isFieldInvalid(fieldName: string): boolean {
    const field = this.userForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.userForm.get(fieldName);
    if (field && field.errors && field.touched) {
      if (field.errors['required']) return `${fieldName} est requis`;
      if (field.errors['email']) return 'Format d\'email invalide';
      if (field.errors['minlength']) return `${fieldName} doit contenir au moins ${field.errors['minlength'].requiredLength} caractères`;
    }
    return '';
  }

  constructor(private userService: UserService, private router: Router, private formBuilder: FormBuilder){}

  

 
}
