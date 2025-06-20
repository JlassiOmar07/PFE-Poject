import { Routes } from '@angular/router';
import { UsersComponent } from './users/users.component';
import { AddUserComponent } from './add-user/add-user.component';
import { LoginComponent } from './login/login.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { RechercheParRoleComponent } from './recherche-par-role/recherche-par-role.component';
import { RechercheParNomComponent } from './recherche-par-nom/recherche-par-nom.component';
import { TasksComponent } from './tasks/tasks.component';
import { ForbiddenComponent } from './service/forbidden/forbidden.component';
import { userGuard } from './guards/user.guard';
import { SuivreTaskComponent } from './suivre-task/suivre-task.component';
import { TestingWorkspaceComponent } from './testing-workspace/testing-workspace.component';
import { TestStepsComponent } from './test-steps/test-steps.component';
import { AllTasksComponent } from './all-tasks/all-tasks.component';
import { TestSuiteDashboardComponent } from './test-suite-dashboard/test-suite-dashboard.component';
import { AcceuilComponent } from './acceuil/acceuil.component';


export const routes: Routes = [
    {path: 'app-forbidden', component: ForbiddenComponent},
    {path: "acceuil", component: AcceuilComponent},
    {path: "login",component: LoginComponent},
    {path: "users", component: UsersComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "add-user", component: AddUserComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "updateUser/:id", component: UpdateUserComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "rechercheParRole", component : RechercheParRoleComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "rechercheParNom", component : RechercheParNomComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "tasks", component: TasksComponent, canActivate:[userGuard], data: { adminOnly: true }},
    {path: "suivreTask", component: SuivreTaskComponent},
    {path: "TestingWorkspace", component : TestingWorkspaceComponent, canActivate:[userGuard], data: { userOnly: true }},
    {path: "testSteps", component: TestStepsComponent, canActivate:[userGuard], data: { userOnly: true }},
    {path: "allTasks", component: AllTasksComponent, canActivate:[userGuard], data: { userOnly: true }},
    {path: "test-suite-dashboard/mission/:missionId/user/:userId", component: TestSuiteDashboardComponent,canActivate:[userGuard], data: { adminOnly: true } },
    {path: "test-suite-dashboard/:id", component: TestSuiteDashboardComponent },
    {path: "**", redirectTo: "/acceuil", pathMatch: "full"}
    
];