import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { Role } from '../model/role.model';
import { Observable, catchError, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Exigences } from '../model/exigence.model';
import { TestSuite } from '../model/TestSuite.model';

const httpOptions = {
headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};


@Injectable({
  providedIn: 'root'
})
export class UserService {

  apiURL : string = 'http://localhost:8080/devtesters'

  users!: User[];
  roles! : Role[];
  user!: User;


  listeRoleNames(): Observable<string[]> {
    return this.listeRoles().pipe(
      map(roles => roles.map(r => r.name!))
    );
  }

  constructor(private http : HttpClient, private authService: AuthService){

  }

  listeUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiURL+"/all" );
  }
  ajouterUser(usr: User):Observable<User> {

    return this.http.post<User>(this.apiURL+"/adduser", usr);
  }

  deleteUser(id: number){
    const url = `${this.apiURL}/deluser/${id}`;
    return this.http.delete(url);
  }

  consulterUser(id: number):Observable<User>{
    const url = `${this.apiURL}/getbyid/${id}`;
    return this.http.get<User>(url);
  }

  updateUser(usr: User):Observable<User>{

    const url = `${this.apiURL}/updateuser`;
    return this.http.put<User>(url, usr);
  }


  listeRoles():Observable<Role[]>{

    return this.http.get<Role[]>(this.apiURL+"/api/role" );
  }

  consulterRole(id: number):Observable <Role>{
    const url = `${this.apiURL}/api/role/${id}`;
    return this.http.get<User>(url);
  }

  addRoleToUser(email: string , rolename : string):Observable<User>{
    const url = `${this.apiURL}/user/${email}/roles/${rolename}`;
    return this.http.post<User>(url, null);
  }

  rechercherParRole(idRole : number):Observable<User[]> {

    const url = `${this.apiURL}/byroles/${idRole}`;
    return this.http.get<User[]>(url);
  }

  // rechercheParRole(nom: string):Observable<User[]>{
  //   const url = `${this.apiURL}/rolesByName/${nom}`;
  //   return this.http.get<User[]>(url);
  // }


  createExigence(mission : Exigences):Observable<Exigences>{
    const url = `${this.apiURL}/addmission`;
    return this.http.post<Exigences>(url, mission);
  }

  addMissionToUser(idUser : number, idEx: string){
    const url = `${this.apiURL}/user/${idUser}/mission/${idEx}`;
    return this.http.post<User>(url,null);
  }



  listeTasks(): Observable<Exigences[]> {
    return this.http.get<Exigences[]>(this.apiURL+"/mission" );
  }

  deleteMission(id: number){
    const url = `${this.apiURL}/delMission/${id}`;
    return this.http.delete(url);
  }

  getUserWithMissions(idMission : number):Observable<User[]>{
    const url = `${this.apiURL}/usersMissions/${idMission}`;
    return this.http.get<User[]>(url);
  }

  updateMission(mission: Exigences):Observable<Exigences>{

    const url = `${this.apiURL}/updatemission`;
    return this.http.put<Exigences>(url, mission);
  }

  getAllMissionOfUser(email: string):Observable<Exigences[]>{
    const url = `${this.apiURL}/userMission/${email}`;
    return this.http.get<Exigences[]>(url)

  }

  // Vérifier si un email existe déjà dans la base de données
  checkEmailExists(email: string): Observable<boolean> {
    const url = `${this.apiURL}/checkEmail/${email}`;
    return this.http.get<boolean>(url).pipe(
      catchError(error => {
        console.error('Erreur lors de la vérification de l\'email', error);
        return of(false); // En cas d'erreur, on considère que l'email n'existe pas
      })
    );
  }

  
  


  launchIDE(): Observable<string> {
    const url = `${this.apiURL}/ide/launch`;

    // Envoi de la requête au backend
    return this.http.post(url, {}, { responseType: 'text' }).pipe(
      map(response => {
        console.log(`[UserService] IDE lancé avec succès: ${response}`);
        return response;
      }),
      catchError(error => {
        if (error.status === 0) {
          console.error('[UserService] Le serveur est inaccessible');
        } else {
          console.error(`[UserService] Statut: ${error.status}, Message: ${error.message}`);
        }

        throw error;
      })
    );
  }

  executeTests(testSuite: TestSuite, missionId?: number):Observable<string>{
    const payload = {
      tag: testSuite.tag,
      name: testSuite.name,
      userId: testSuite.user?.id?.toString() || '',
      missionId: missionId?.toString() || null
    };
    const url = `${this.apiURL}/execute`;
    return this.http.post(url, payload, { responseType: 'text' });

  }

   GetAllTests():Observable<TestSuite[]>{
    const url = `${this.apiURL}/history`;
    return this.http.get<TestSuite[]>(url)
  }

  /**
   * Récupère tous les TestSuites associés à une mission spécifique
   * @param missionId ID de la mission
   */
  getTestSuitesByMission(missionId: number): Observable<TestSuite[]> {
    const url = `${this.apiURL}/mission/${missionId}/tests`;
    return this.http.get<TestSuite[]>(url);
  }

  /**
   * Récupère le TestSuite le plus récent pour une mission spécifique
   * @param missionId ID de la mission
   */
  getLatestTestSuiteByMission(missionId: number): Observable<TestSuite> {
    const url = `${this.apiURL}/mission/${missionId}/latest-test`;
    return this.http.get<TestSuite>(url);
  }

  /**
   * Marque une mission comme terminée
   * @param missionId ID de la mission à marquer comme terminée
   */
  markMissionAsCompleted(missionId: number): Observable<Exigences> {
    const url = `${this.apiURL}/mission/${missionId}/complete`;
    return this.http.put<Exigences>(url, {});
  }


  getTestsByMissionAndUser(missionId: number, userId: number): Observable<TestSuite[]> {
    const url = `${this.apiURL}/mission/${missionId}/user/${userId}/tests`;
    return this.http.get<TestSuite[]>(url);
  }

  /**
   * Exécute tous les tests Cucumber sans filtrage par tag (version synchrone)
   * @param name Le nom descriptif de cette exécution
   */
  executeAllTests(testSuite: TestSuite): Observable<string> {
    const payload = {
      name: testSuite.name
    };
    const url = `${this.apiURL}/execute-all`;
    return this.http.post(url, payload, { responseType: 'text' });
  }

  /**
   * Récupère le dernier TestSuite global (sans tag, userId et missionId)
   * Utilise l'endpoint spécifique du backend
   */
  getLatestGlobalTestSuite(): Observable<TestSuite> {
    const url = `${this.apiURL}/history/latest-standalone-test`;
    return this.http.get<TestSuite>(url).pipe(
      map(testSuite => {
        return testSuite;
      }),
      catchError(error => {
        console.error('❌ Erreur lors de la récupération du TestSuite global:', error);
        throw error;
      })
    );
  }

  
  

}
