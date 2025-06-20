package com.uib.CodeCycle.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;          // Nom du rôle (ex. "TESTEUR", "DEVELOPPEUR", "ADMIN")
    private String description;   // Description du rôle, ex. "Responsable des tests fonctionnels"



}
