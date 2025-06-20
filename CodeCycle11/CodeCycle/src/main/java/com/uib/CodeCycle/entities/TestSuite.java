package com.uib.CodeCycle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TestSuite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                  // Nom donné à cette suite de tests
    private String tag;                   // Tag Cucumber utilisé pour filtrer les tests (@ps, @cnx, etc.)
    private LocalDateTime executionDate;  // Date et heure de l'exécution
    private Long executionTimeMs;         // Durée totale d'exécution en millisecondes

    private Integer totalTests;           // Nombre total de scénarios exécutés
    private Integer passedTests;          // Nombre de scénarios réussis
    private Integer failedTests;          // Nombre de scénarios échoués
    private Integer skippedTests;         // Nombre de scénarios ignorés
    private Double successRate;           // Taux de réussite (pourcentage)

    @Lob
    @Column(length = 100000)
    private String logs;                  // Logs complets de l'exécution

    @Lob
    @Column(length = 100000)
    private String jsonReport;            // Rapport Cucumber au format JSON (pour analyse détaillée)
    
    // Relation avec UserEntity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;              // L'utilisateur qui a exécuté cette suite de tests
    
    // Relation avec UserMission
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mission_id")
    private UserMission mission;          // La mission associée à cette suite de tests
}
