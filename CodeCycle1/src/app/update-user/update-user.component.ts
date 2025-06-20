import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../model/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { Role } from '../model/role.model';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-update-user',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './update-user.component.html',
  styleUrl: './update-user.component.css'
})
export class UpdateUserComponent implements OnInit{

  userForm!: FormGroup;
  currentUser = new User();
  roles!: Role[];
  updatedRoleName!: string;
  originalRoleName!: string; // Pour garder une trace du rôle original
  originalEmail!: string; // Pour garder une trace de l'email original
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ){}

  ngOnInit(): void {
    this.initializeForm();
    this.userService.listeRoles().subscribe(r =>{
      this.roles = r;
    });
    this.userService.consulterUser(this.activatedRoute.snapshot.params['id']).
    subscribe(usr =>{
      this.currentUser = usr;
      this.originalEmail = usr.email; // Sauvegarder l'email original

      // Initialiser le rôle sélectionné avec le premier rôle de l'utilisateur
      if (this.currentUser.roles && this.currentUser.roles.length > 0) {
        this.originalRoleName = this.currentUser.roles[0].name!;
        this.updatedRoleName = this.originalRoleName; // Pré-sélectionner le rôle actuel
        console.log('Rôle actuel:', this.originalRoleName);
      }

      // Remplir le formulaire avec les données de l'utilisateur
      this.userForm.patchValue({
        firstName: usr.firstName,
        lastName: usr.lastName,
        email: usr.email,
        password: '', // Laisser vide pour ne pas modifier le mot de passe
        roleName: this.originalRoleName
      });
    });
  }

  private initializeForm(): void {
    this.userForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: [''], // Optionnel pour la mise à jour
      roleName: ['', Validators.required]
    });
  }

  /**
   * Vérifie si un rôle donné est le rôle actuel de l'utilisateur
   */
  isCurrentRole(roleName: string | undefined): boolean {
    if (!roleName || !this.originalRoleName) return false;
    return this.originalRoleName === roleName;
  }

  /**
   * Vérifie si le rôle sélectionné est différent du rôle original
   */
  hasRoleChanged(): boolean {
    return this.userForm.value.roleName !== this.originalRoleName;
  }

  /**
   * Retourne le nom du rôle actuel
   */
  getCurrentRoleName(): string {
    return this.originalRoleName ;
  }

  async updatedUser(){
    if (this.userForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    try {
      // Vérifier si l'email a changé et s'il existe déjà
      if (this.userForm.value.email !== this.originalEmail) {
        const emailExists = await firstValueFrom(this.userService.checkEmailExists(this.userForm.value.email));
        if (emailExists) {
          this.errorMessage = `Un utilisateur avec l'email '${this.userForm.value.email}' existe déjà`;
          this.isSubmitting = false;
          return;
        }
      }

      // Préparer les données pour le backend
      const userToUpdate = new User();
      userToUpdate.id = this.currentUser.id;
      userToUpdate.email = this.userForm.value.email;
      userToUpdate.firstName = this.userForm.value.firstName;
      userToUpdate.lastName = this.userForm.value.lastName;
      userToUpdate.password = this.userForm.value.password || ''; // Envoyer une chaîne vide si pas de nouveau mot de passe

      console.log('Données utilisateur à envoyer:', userToUpdate);

      this.userService.updateUser(userToUpdate).subscribe({
        next: () => {
          // Changer le rôle SEULEMENT si un nouveau rôle différent est sélectionné
          if (this.hasRoleChanged() && this.userForm.value.roleName) {
            this.userService.addRoleToUser(this.userForm.value.email, this.userForm.value.roleName).subscribe({
              next: () => {
                alert(`Utilisateur mis à jour et le rôle "${this.userForm.value.roleName}" changé avec succès `);
                this.router.navigate(['users']);
              },
              error: () => {
                alert('Utilisateur mis à jour, mais erreur lors du changement de rôle');
                this.router.navigate(['users']);
              }
            });
          } else {
            alert('Utilisateur mis à jour avec succès');
            this.router.navigate(['users']);
          }
        }
      });
    } catch (error) {
      this.errorMessage = 'Erreur lors de la vérification de l\'email';
      this.isSubmitting = false;
    }
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


}
