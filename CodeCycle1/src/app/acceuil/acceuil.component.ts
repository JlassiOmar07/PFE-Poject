import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-acceuil',
  imports: [CommonModule],
  templateUrl: './acceuil.component.html',
  styleUrl: './acceuil.component.css'
})
export class AcceuilComponent implements OnInit {

  showFallback = false;

  ngOnInit(): void {
    // Simuler le chargement des statistiques
    //this.loadStats();
  }

  onImageError(event: any): void {
    console.log('Image non trouvée, affichage du fallback');
    this.showFallback = true;
    // Cacher l'image qui a échoué
    event.target.style.display = 'none';
  }

  // private loadStats(): void {
  //   // Simuler des statistiques (vous pouvez les connecter à votre API plus tard)
  //   setTimeout(() => {
  //     this.stats = {
  //       users: 25,
  //       tasks: 12,
  //       completed: 48,
  //       tests: 156
  //     };
  //   }, 500);
  // }
}
