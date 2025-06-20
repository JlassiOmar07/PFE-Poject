import { Component } from '@angular/core';
import { Exigences } from '../model/exigence.model';
import { User } from '../model/user.model';
import { UserService } from '../service/user.service';
import { AuthService } from '../service/auth.service';
import { TasksComponent } from '../tasks/tasks.component';

@Component({
  selector: 'app-suivre-task',
  imports: [],
  templateUrl: './suivre-task.component.html',
  styleUrl: './suivre-task.component.css'
})
export class SuivreTaskComponent {

      tasks!: Exigences[];
      users!: User[];
      selectedTask! : Exigences;
      currentUser! : string;
      Missions! : Exigences[];
    
      constructor(private userService: UserService,public tasksComponent: TasksComponent){
    
      }
    
      ngOnInit(): void {
       
    
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
  
  
  
    getMissionOfUSer(){
      if (this.tasksComponent.selectedUser && this.tasksComponent.selectedUser.email) {
        this.currentUser = this.tasksComponent.selectedUser.email;
        this.userService.getAllMissionOfUser(this.currentUser).subscribe(mission => {
          this.Missions = mission;
        });
      } else {
        // Handle the case where selectedUser is not set
        this.currentUser = '';
        this.Missions = [];
      }
  }

}
