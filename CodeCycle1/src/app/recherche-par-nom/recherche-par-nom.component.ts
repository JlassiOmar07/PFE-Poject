import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../service/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recherche-par-nom',
  imports: [FormsModule, CommonModule],
  templateUrl: './recherche-par-nom.component.html',
  styleUrls: ['./recherche-par-nom.component.css'],
})
export class RechercheParNomComponent implements OnInit {
  

  users!: User[];

  searchTerm!: string;


  constructor(private userService: UserService){

  }
  
  ngOnInit(): void {
    this.userService.listeUsers().
   subscribe(u => {
    this.users = u ;
   });
  }

  onKeyUp(filterText: string) {
    const text = filterText.trim().toLowerCase();
    if (!text) {
      // si recherche vide → on remet l'original
      this.userService.listeUsers().
      subscribe(u => {
       this.users = u ;
      });
    } else {
      // sinon on filtre sur la liste complète
      this.users = this.users.filter(item =>
        item.firstName.toLowerCase().includes(text)
      );
    }
  }

}
