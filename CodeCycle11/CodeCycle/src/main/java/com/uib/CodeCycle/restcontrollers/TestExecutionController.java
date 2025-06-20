package com.uib.CodeCycle.restcontrollers;

import com.uib.CodeCycle.entities.TestSuite;
import com.uib.CodeCycle.services.TestExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "*")
public class TestExecutionController {

    @Autowired
    private TestExecutionService testExecutionService;

    /**
     * Exécute les tests Cucumber avec le tag spécifié
     */
    @PostMapping("/execute")
    public ResponseEntity<String> executeTests(@RequestBody Map<String, String> payload) {
        String tag = payload.getOrDefault("tag", "@ps");
        String name = payload.getOrDefault("name", "Test Execution - " + tag);
        
        // Récupérer l'ID de l'utilisateur depuis la payload
        Long userId = null;
        if (payload.containsKey("userId")) {
            try {
                userId = Long.parseLong(payload.get("userId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid user ID format");
            }
        }

        // Récupérer l'ID de la mission depuis la payload
        Long missionId = null;
        if (payload.containsKey("missionId")) {
            try {
                missionId = Long.parseLong(payload.get("missionId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid mission ID format");
            }
        }
        
        CompletableFuture<TestSuite> future = testExecutionService.executeTests(tag, name, userId, missionId);
        
        return ResponseEntity.accepted().body("Test execution started with tag: " + tag);
    }

    /**
     * Exécute tous les tests Cucumber sans filtrage par tag
     */
    @PostMapping("/execute-all")
    public ResponseEntity<String> executeAllTests(@RequestBody Map<String, String> payload) {
        String name = payload.getOrDefault("name", "All Tests Execution");

        // Récupérer l'ID de l'utilisateur depuis la payload
        Long userId = null;
        if (payload.containsKey("userId")) {
            try {
                userId = Long.parseLong(payload.get("userId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid user ID format");
            }
        }

        // Récupérer l'ID de la mission depuis la payload
        Long missionId = null;
        if (payload.containsKey("missionId")) {
            try {
                missionId = Long.parseLong(payload.get("missionId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid mission ID format");
            }
        }

        // Vérifiez si missionId est bien passé ici
        CompletableFuture<TestSuite> future = testExecutionService.executeAllTests(name, userId, missionId);

        // Ajouter un callback pour logger quand c'est terminé
        future.thenAccept(result -> {
            System.out.println("=== EXECUTION ASYNCHRONE TERMINÉE ===");
            System.out.println("TestSuite sauvegardé avec ID: " + result.getId());
        }).exceptionally(throwable -> {
            System.out.println("=== ERREUR EXECUTION ASYNCHRONE ===");
            throwable.printStackTrace();
            return null;
        });

        return ResponseEntity.accepted().body("Execution of all tests started with name: " + name +
                                             ", userId: " + userId +
                                             ", missionId: " + missionId);
    }

    /**
     * Exécute tous les tests Cucumber sans filtrage par tag (version synchrone pour débogage)
     */
    @PostMapping("/execute-all-sync")
    public ResponseEntity<String> executeAllTestsSync(@RequestBody Map<String, String> payload) {
        String name = payload.getOrDefault("name", "All Tests Execution");

        // Récupérer l'ID de l'utilisateur depuis la payload
        Long userId = null;
        if (payload.containsKey("userId")) {
            try {
                userId = Long.parseLong(payload.get("userId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid user ID format");
            }
        }

        // Récupérer l'ID de la mission depuis la payload
        Long missionId = null;
        if (payload.containsKey("missionId")) {
            try {
                missionId = Long.parseLong(payload.get("missionId"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid mission ID format");
            }
        }

        try {
            // Version synchrone pour le débogage avec timeout de 30 minutes
            TestSuite result = testExecutionService.executeAllTests(name, userId, missionId)
                .get(30, java.util.concurrent.TimeUnit.MINUTES);
            return ResponseEntity.ok("Test execution completed with ID: " + result.getId());
        } catch (java.util.concurrent.TimeoutException e) {
            return ResponseEntity.status(408).body("Test execution timeout after 30 minutes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error executing tests: " + e.getMessage());
        }
    }


    /**
     * Récupère l'historique complet des exécutions de tests
     */
    @GetMapping("/history")
    public ResponseEntity<List<TestSuite>> getTestHistory() {
        List<TestSuite> history = testExecutionService.getTestHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * Récupère les N dernières exécutions de tests
     */
    @GetMapping("/history/recent")
    public ResponseEntity<List<TestSuite>> getRecentTestHistory(@RequestParam(defaultValue = "5") int limit) {
        List<TestSuite> recentHistory = testExecutionService.getRecentTestHistory(limit);
        return ResponseEntity.ok(recentHistory);
    }

    /**
     * Récupère une exécution de test spécifique par son ID
     */
    @GetMapping("/history/{id}")
    public ResponseEntity<TestSuite> getTestSuiteById(@PathVariable Long id) {
        TestSuite testSuite = testExecutionService.getTestSuiteById(id);
        if (testSuite != null) {
            return ResponseEntity.ok(testSuite);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère l'historique des exécutions pour un tag spécifique
     */
    @GetMapping("/history/tag/{tag}")
    public ResponseEntity<List<TestSuite>> getTestHistoryByTag(@PathVariable String tag) {
        List<TestSuite> historyByTag = testExecutionService.getTestHistoryByTag(tag);
        return ResponseEntity.ok(historyByTag);
    }

    /**
     * Récupère l'historique des exécutions pour tous les tests (sans tag spécifique)
     */
    @GetMapping("/history/all-tests")
    public ResponseEntity<List<TestSuite>> getAllTestsHistory() {
        List<TestSuite> historyByTag = testExecutionService.getTestHistoryByTag(null);
        return ResponseEntity.ok(historyByTag);
    }



    /**
     * Récupère l'historique des exécutions pour un utilisateur spécifique
     */
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<TestSuite>> getTestHistoryByUser(@PathVariable Long userId) {
        List<TestSuite> historyByUser = testExecutionService.getTestHistoryByUser(userId);
        return ResponseEntity.ok(historyByUser);
    }

    /**
     * Récupère tous les tests associés à une mission
     */
    @GetMapping("/mission/{missionId}/tests")
    public ResponseEntity<List<TestSuite>> getTestsByMission(@PathVariable Long missionId) {
        List<TestSuite> tests = testExecutionService.getTestsByMission(missionId);
        return ResponseEntity.ok(tests);
    }

    /**
     * Récupère le dernier test exécuté pour une mission
     */
    @GetMapping("/mission/{missionId}/latest-test")
    public ResponseEntity<TestSuite> getLatestTestByMission(@PathVariable Long missionId) {
        TestSuite latestTest = testExecutionService.getLatestTestByMission(missionId);
        if (latestTest != null) {
            return ResponseEntity.ok(latestTest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère tous les tests associés à une mission et un utilisateur
     */
    @GetMapping("/mission/{missionId}/user/{userId}/tests")
    public ResponseEntity<List<TestSuite>> getTestsByMissionAndUser(
        @PathVariable Long missionId,
        @PathVariable Long userId
    ) {
        List<TestSuite> tests = testExecutionService.getTestsByMissionAndUser(missionId, userId);
        return ResponseEntity.ok(tests);
    }

    /**
     * Récupère le dernier test exécuté pour une mission et un utilisateur
     */
    @GetMapping("/mission/{missionId}/user/{userId}/latest-test")
    public ResponseEntity<TestSuite> getLatestTestByMissionAndUser(
        @PathVariable Long missionId,
        @PathVariable Long userId
    ) {
        TestSuite latestTest = testExecutionService.getLatestTestByMissionAndUser(missionId, userId);
        if (latestTest != null) {
            return ResponseEntity.ok(latestTest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère le dernier test exécuté sans tag, sans utilisateur et sans mission
     */
    @GetMapping("/history/latest-standalone-test")
    public ResponseEntity<TestSuite> getLatestStandaloneTest() {
        TestSuite latestTest = testExecutionService.getLatestTestWithoutTagUserMission();
        if (latestTest != null) {
            return ResponseEntity.ok(latestTest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
