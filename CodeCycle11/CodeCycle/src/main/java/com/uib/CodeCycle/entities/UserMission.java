package com.uib.CodeCycle.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mission")
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String idExigence;
    private String titre;
    private String description;
    private String commentaire;
    private boolean completed = false;
    
    @ManyToMany(mappedBy = "userMission")
    @JsonIgnore
    private List<UserEntity> users;
    
    // Relation avec TestSuite
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TestSuite> testSuites = new ArrayList<>();
}
