package com.uib.CodeCycle.repos;

import com.uib.CodeCycle.entities.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestSuiteRepository extends JpaRepository<TestSuite, Long> {
    List<TestSuite> findByTag(String tag);

    // Méthode pour récupérer les tests sans tag (tag = null)
    List<TestSuite> findByTagIsNull();

    @Query("SELECT t FROM TestSuite t ORDER BY t.executionDate DESC")
    List<TestSuite> findAllOrderByExecutionDateDesc();
    
    @Query(value = "SELECT * FROM test_suite ORDER BY execution_date DESC LIMIT :limit", nativeQuery = true)
    List<TestSuite> findLastNTestSuites(@Param("limit") int limit);
    
    @Query("SELECT AVG(t.successRate) FROM TestSuite t WHERE t.tag = :tag")
    Double getAverageSuccessRateByTag(@Param("tag") String tag);
    
    // Méthode pour récupérer les tests par utilisateur
    List<TestSuite> findByUserId(Long userId);
    
    // Nouvelles méthodes pour les missions
    List<TestSuite> findByMissionId(Long missionId);
    
    @Query("SELECT t FROM TestSuite t WHERE t.mission.id = :missionId ORDER BY t.executionDate DESC")
    List<TestSuite> findByMissionIdOrderByExecutionDateDesc(@Param("missionId") Long missionId);
    
    @Query(value = "SELECT * FROM test_suite WHERE mission_id = :missionId ORDER BY execution_date DESC LIMIT 1", nativeQuery = true)
    TestSuite findLatestByMissionId(@Param("missionId") Long missionId);
    
    // Récupérer les tests par mission et utilisateur
    @Query("SELECT t FROM TestSuite t WHERE t.mission.id = :missionId AND t.user.id = :userId ORDER BY t.executionDate DESC")
    List<TestSuite> findByMissionIdAndUserIdOrderByExecutionDateDesc(
        @Param("missionId") Long missionId, 
        @Param("userId") Long userId
    );

    // Récupérer le dernier test par mission et utilisateur
    @Query(value = "SELECT * FROM test_suite WHERE mission_id = :missionId AND user_id = :userId ORDER BY execution_date DESC LIMIT 1", nativeQuery = true)
    TestSuite findLatestByMissionIdAndUserId(
        @Param("missionId") Long missionId, 
        @Param("userId") Long userId
    );

    /**
     * Récupère le dernier test exécuté sans tag, sans utilisateur et sans mission
     */
    @Query(value = "SELECT * FROM test_suite WHERE tag IS NULL AND user_id IS NULL AND mission_id IS NULL ORDER BY execution_date DESC LIMIT 1", nativeQuery = true)
    TestSuite findLatestTestWithoutTagUserMission();
}
