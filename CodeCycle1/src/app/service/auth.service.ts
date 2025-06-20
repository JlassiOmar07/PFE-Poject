import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user.model';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiURL: string = 'http://localhost:8080/devtesters' ;
  token!: string ;

  constructor(private router:Router, private http : HttpClient) { }

  public loggedUser! :string ;
  public loggedUserInfo!: User; // Informations complètes de l'utilisateur
  public isloggedIn : boolean = false;
  public roles!:string[];
  private helper = new JwtHelperService();

  login(user : User) {
    return this.http.post<User>(this.apiURL+'/login', user , {observe:'response'});
  }

  /**
   * Récupère un utilisateur par son email
  */
  getUserByEmail(email: string){
    return this.http.get<User>(`${this.apiURL}/user/email/${email}`);
  }

  getToken():string {
    return this.token;
  }



  saveToken(jwt:string){
    localStorage.setItem('jwt',jwt);
    this.token = jwt;
    this.isloggedIn = true;
    this.decodeJWT();
}

  decodeJWT() {
    if (this.token == undefined)
      return;
    const decodedToken = this.helper.decodeToken(this.token);
    this.roles = decodedToken.roles;
    console.log("roles : "+this.roles);
    this.loggedUser = decodedToken.sub;
    console.log("email : "+decodedToken.sub);

    // Récupérer les informations complètes de l'utilisateur
    this.loadUserInfo();
  }

  /**
   * Récupère les informations complètes de l'utilisateur connecté
   */
  private loadUserInfo() {
    if (this.loggedUser) {
      this.getUserByEmail(this.loggedUser).subscribe({
        next: (user) => {
          this.loggedUserInfo = user;
        },
        error: (error) => {
          console.error("Erreur lors du chargement des informations utilisateur:", error);
        }
      });
    }
  }

  loadToken() {
    this.token = localStorage.getItem('jwt')!;
    this.decodeJWT();
  }



  /**
   * Retourne le nom d'affichage de l'utilisateur connecté
   * Format: "Prénom Nom" ou l'email si les informations ne sont pas disponibles
   */
  getDisplayName(): string {
   
    return `${this.loggedUserInfo.firstName} ${this.loggedUserInfo.lastName}`;
    
  }

  /**
   * Retourne les initiales de l'utilisateur pour l'avatar
   */
  getUserInitials(): string {
    return `${this.loggedUserInfo.firstName.charAt(0)}${this.loggedUserInfo.lastName.charAt(0)}`.toUpperCase();
    
    
  }

logout() {
  this.loggedUser = undefined!;
  this.loggedUserInfo = undefined!;
  this.roles = undefined!;
  this.token= undefined!;
  this.isloggedIn = false;
  localStorage.removeItem('jwt');
  this.router.navigate(['/login']);

}



  isAdmin():Boolean{
    if (!this.roles) {
      return false;
    }
    //console.log((this.roles.indexOf('ADMIN') >=0));
    return  (this.roles.indexOf('ADMIN') >=0);
  }

  isUser():Boolean{
    if (!this.roles) {
      return false;
    }
    return  (this.roles.indexOf('USER') >=0);
  }

  isTokenExpired(): Boolean
  {
    return  this.helper.isTokenExpired(this.token);
  }

}
