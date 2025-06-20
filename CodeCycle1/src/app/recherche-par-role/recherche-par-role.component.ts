import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { Role } from '../model/role.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-recherche-par-role',
  imports: [CommonModule,FormsModule],
  templateUrl: './recherche-par-role.component.html',
  styles: ``
})
export class RechercheParRoleComponent implements OnInit{
  
  users!: User[];
  idRole!: number;
  roles!: Role[];

  constructor(private userService: UserService){

  }

  ngOnInit(): void {
    this.userService.listeRoles().subscribe(r => {
      this.roles = r;
    });
  }

  onChange(){
    this.userService.rechercherParRole(this.idRole).subscribe(usr => {
      this.users = usr;
    });
  }

}
