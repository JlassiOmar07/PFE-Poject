package com.uib.CodeCycle.services;

import com.uib.CodeCycle.controllers.TestEventController;
import com.uib.CodeCycle.entities.TestSuite;
import com.uib.CodeCycle.repos.MissionRepository;
import com.uib.CodeCycle.repos.TestSuiteRepository;
import com.uib.CodeCycle.repos.UserEntityRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TestExecutionService {

    private static final String PROJECT_PATH = "C:\\workspace\\testautomation\\webDriver_java\\UibTestProjectJUNIT";
    private static final String REPORT_PATH = PROJECT_PATH + "\\target\\cucumber-report.json";

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private TestEventController testEventController;

    /**
     * Exécute les tests Cucumber avec le tag spécifié
     * @param tag Le tag Cucumber à utiliser (ex: "@ps")
     * @param name Le nom descriptif de cette exécution
     * @param userId L'ID de l'utilisateur qui exécute les tests (peut être null)
     * @param missionId L'ID de la mission associée (peut être null)
     * @return Un CompletableFuture contenant le TestSuite avec les résultats
     */
    public CompletableFuture<TestSuite> executeTests(String tag, String name, Long userId, Long missionId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("=== DEBUT executeAllTests ===");
            System.out.println("Name: " + name);
            System.out.println("UserId: " + userId);
            System.out.println("MissionId: " + missionId);
            System.out.println("Tag: " + tag);
            TestSuite testSuite = new TestSuite();
            testSuite.setName(name);
            testSuite.setTag(tag);
            testSuite.setExecutionDate(LocalDateTime.now());
            
            // Associer l'utilisateur à la suite de tests
            if (userId != null) {
                userEntityRepository.findById(userId).ifPresent(testSuite::setUser);
            }
            
            // Associer la mission à la suite de tests
            if (missionId != null) {
                missionRepository.findById(missionId).ifPresent(testSuite::setMission);
            }

            StringBuilder logs = new StringBuilder();
            // Mesurer le temps d'exécution
            long startTime = System.currentTimeMillis();

            try {
                System.out.println("=== EXECUTION DES TESTS ===");

                // Configurer le processus Maven avec le chemin complet
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(new File(PROJECT_PATH));
                processBuilder.command("C:\\Users\\jelas\\Downloads\\apache-maven-3.9.9-bin\\apache-maven-3.9.9\\bin\\mvn.cmd", "clean", "test", "-Dcucumber.filter.tags=" + tag);


                // Démarrer le processus
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Lire les logs
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.append(line).append("\n");
                }

                // Attendre la fin du processus
                int exitCode = process.waitFor();
                long endTime = System.currentTimeMillis();

                // Lire et analyser le rapport Cucumber
                String jsonReport = readCucumberReport();

                // Calculer les métriques
                int totalTests = countTotalTests(jsonReport);
                int passedTests = countPassedTests(jsonReport);
                int failedTests = countFailedTests(jsonReport);
                int skippedTests = totalTests - (passedTests + failedTests);
                double successRate = totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;

                // Remplir l'objet TestSuite
                testSuite.setLogs(logs.toString());
                testSuite.setJsonReport(jsonReport);
                testSuite.setExecutionTimeMs(endTime - startTime);
                testSuite.setTotalTests(totalTests);
                testSuite.setPassedTests(passedTests);
                testSuite.setFailedTests(failedTests);
                testSuite.setSkippedTests(skippedTests);
                testSuite.setSuccessRate(successRate);

                // Sauvegarder dans la base de données
                return testSuiteRepository.save(testSuite);

            } catch (Exception e) {
                e.printStackTrace();
                testSuite.setLogs("Error executing tests: " + e.getMessage());
                return testSuiteRepository.save(testSuite);
            }
        });
    }

    /**
     * Lit le rapport JSON généré par Cucumber
     */
    private String readCucumberReport() {
        try {
            File reportFile = new File(REPORT_PATH);

            if (!reportFile.exists()) {
                return "[]";
            }

            return new String(Files.readAllBytes(reportFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Compte le nombre total de scénarios dans le rapport
     */
    private int countTotalTests(String jsonReport) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonReport);

            int count = 0;
            for (JsonNode feature : rootNode) {
                JsonNode elements = feature.get("elements");
                if (elements != null) {
                    for (JsonNode element : elements) {
                        // Ne compter que les scénarios, pas les backgrounds
                        if (element.has("type") && "scenario".equals(element.get("type").asText())) {
                            count++;
                        }
                    }
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Compte le nombre de scénarios réussis
     */
    private int countPassedTests(String jsonReport) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonReport);

            int passedCount = 0;
            for (JsonNode feature : rootNode) {
                JsonNode elements = feature.get("elements");
                if (elements != null) {
                    for (JsonNode element : elements) {
                        if (element.has("type") && "scenario".equals(element.get("type").asText())) {
                            boolean scenarioPassed = true;
                            JsonNode steps = element.get("steps");
                            if (steps != null) {
                                for (JsonNode step : steps) {
                                    JsonNode result = step.get("result");
                                    if (result != null && !"passed".equals(result.get("status").asText())) {
                                        scenarioPassed = false;
                                        break;
                                    }
                                }
                            }
                            if (scenarioPassed) {
                                passedCount++;
                            }
                        }
                    }
                }
            }
            return passedCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Compte le nombre de scénarios échoués
     */
    private int countFailedTests(String jsonReport) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonReport);

            int failedCount = 0;
            for (JsonNode feature : rootNode) {
                JsonNode elements = feature.get("elements");
                if (elements != null) {
                    for (JsonNode element : elements) {
                        if (element.has("type") && "scenario".equals(element.get("type").asText())) {
                            boolean scenarioFailed = false;
                            JsonNode steps = element.get("steps");
                            if (steps != null) {
                                for (JsonNode step : steps) {
                                    JsonNode result = step.get("result");
                                    if (result != null && "failed".equals(result.get("status").asText())) {
                                        scenarioFailed = true;
                                        break;
                                    }
                                }
                            }
                            if (scenarioFailed) {
                                failedCount++;
                            }
                        }
                    }
                }
            }
            return failedCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Récupère l'historique complet des exécutions de tests
     */
    public List<TestSuite> getTestHistory() {
        return testSuiteRepository.findAllOrderByExecutionDateDesc();
    }

    /**
     * Récupère les N dernières exécutions de tests
     */
    public List<TestSuite> getRecentTestHistory(int limit) {
        return testSuiteRepository.findLastNTestSuites(limit);
    }

    /**
     * Récupère une exécution de test spécifique par son ID
     */
    public TestSuite getTestSuiteById(Long id) {
        return testSuiteRepository.findById(id).orElse(null);
    }

    /**
     * Récupère l'historique des exécutions pour un tag spécifique
     */
    public List<TestSuite> getTestHistoryByTag(String tag) {
        if (tag == null) {
            return testSuiteRepository.findByTagIsNull();
        }
        return testSuiteRepository.findByTag(tag);
    }

    /**
     * Calcule le taux de réussite moyen pour un tag spécifique
     */
    public Double getAverageSuccessRateByTag(String tag) {
        return testSuiteRepository.getAverageSuccessRateByTag(tag);
    }

    /**
     * Récupère l'historique des exécutions pour un utilisateur spécifique
     */
    public List<TestSuite> getTestHistoryByUser(Long userId) {
        return testSuiteRepository.findByUserId(userId);
    }

    /**
     * Récupère tous les tests associés à une mission
     */
    public List<TestSuite> getTestsByMission(Long missionId) {
        return testSuiteRepository.findByMissionIdOrderByExecutionDateDesc(missionId);
    }

    /**
     * Récupère le dernier test exécuté pour une mission
     */
    public TestSuite getLatestTestByMission(Long missionId) {
        return testSuiteRepository.findLatestByMissionId(missionId);
    }

    /**
     * Récupère tous les tests associés à une mission et un utilisateur
     */
    public List<TestSuite> getTestsByMissionAndUser(Long missionId, Long userId) {
        return testSuiteRepository.findByMissionIdAndUserIdOrderByExecutionDateDesc(missionId, userId);
    }

    /**
     * Récupère le dernier test exécuté pour une mission et un utilisateur
     */
    public TestSuite getLatestTestByMissionAndUser(Long missionId, Long userId) {
        return testSuiteRepository.findLatestByMissionIdAndUserId(missionId, userId);
    }

    /**
     * Sauvegarde un TestSuite (méthode utilitaire pour les tests)
     */
    public TestSuite saveTestSuite(TestSuite testSuite) {
        return testSuiteRepository.save(testSuite);
    }

    /**
     * Exécute tous les tests Cucumber sans filtrage par tag
     * @param name Le nom descriptif de cette exécution
     * @param userId L'ID de l'utilisateur qui exécute les tests (peut être null)
     * @param missionId L'ID de la mission associée (peut être null)
     * @return Un CompletableFuture contenant le TestSuite avec les résultats
     */
    public CompletableFuture<TestSuite> executeAllTests(String name, Long userId, Long missionId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("=== DEBUT executeAllTests ===");
            System.out.println("Name: " + name);
            System.out.println("UserId: " + userId);
            System.out.println("MissionId: " + missionId);

            TestSuite testSuite = new TestSuite();
            testSuite.setName(name);
            testSuite.setTag(null);  // Pas de tag - tous les tests du projet
            testSuite.setExecutionDate(LocalDateTime.now());

            // Associer l'utilisateur à la suite de tests
            if (userId != null) {
                userEntityRepository.findById(userId).ifPresent(user -> {
                    System.out.println("User trouvé: " + user.getEmail());
                    testSuite.setUser(user);
                });
            }

            // Associer la mission à la suite de tests
            if (missionId != null) {
                missionRepository.findById(missionId).ifPresent(mission -> {
                    System.out.println("Mission trouvée: " + mission.getTitre());
                    testSuite.setMission(mission);
                });
            }

            StringBuilder logs = new StringBuilder();
            // Mesurer le temps d'exécution
            long startTime = System.currentTimeMillis();

            try {
                // Configurer le processus Maven sans filtre de tag
                System.out.println("=== CONFIGURATION MAVEN ===");
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(new File(PROJECT_PATH));
                processBuilder.command("C:\\Users\\jelas\\Downloads\\apache-maven-3.9.9-bin\\apache-maven-3.9.9\\bin\\mvn.cmd", "clean", "test");
                System.out.println("Commande Maven: " + processBuilder.command());
                System.out.println("Répertoire de travail: " + PROJECT_PATH);

                // Démarrer le processus
                System.out.println("=== DÉMARRAGE DU PROCESSUS ===");
                Process process = processBuilder.start();

                // Lire les logs de sortie standard et d'erreur
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                // Lire les logs en parallèle
                System.out.println("=== LECTURE DES LOGS ===");
                StringBuilder errorLogs = new StringBuilder();

                // Thread pour lire les erreurs
                Thread errorThread = new Thread(() -> {
                    try {
                        String errorLine;
                        while ((errorLine = errorReader.readLine()) != null) {
                            errorLogs.append("ERROR: ").append(errorLine).append("\n");
                            System.out.println("MAVEN ERROR: " + errorLine);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                errorThread.start();

                // Lire les logs de sortie standard
                String line;
                boolean isScenarioOutput = false;
                while ((line = reader.readLine()) != null) {
                    logs.append(line).append("\n");

                    // Détecter le début d'un scénario Cucumber
                    if (line.trim().startsWith("Scenario:") || line.trim().startsWith("@")) {
                        isScenarioOutput = true;
                        System.out.println("\n" + line);
                    }
                    // Afficher les étapes du scénario
                    else if (isScenarioOutput && (line.trim().startsWith("Given ") ||
                                                 line.trim().startsWith("When ") ||
                                                 line.trim().startsWith("Then ") ||
                                                 line.trim().startsWith("And ") ||
                                                 line.contains("# steps."))) {
                        System.out.println(line);
                        
                        // Envoyer les étapes au front-end en temps réel via SSE
                        try {
                            testEventController.sendTestStepUpdate(name, line);
                        } catch (Exception e) {
                            System.out.println("Erreur d'envoi SSE: " + e.getMessage());
                        }
                    }
                    // Fin du scénario
                    else if (isScenarioOutput && line.trim().isEmpty()) {
                        isScenarioOutput = false;
                    }

                    // Afficher les lignes importantes
                    if (line.contains("BUILD SUCCESS") || line.contains("BUILD FAILURE") ||
                        line.contains("Tests run:") || line.contains("FAILURE")) {
                        System.out.println("MAVEN: " + line);
                    }
                }

                // Attendre la fin du processus avec timeout
                System.out.println("=== ATTENTE FIN DU PROCESSUS ===");
                boolean finished = process.waitFor(20, java.util.concurrent.TimeUnit.MINUTES);
                long endTime = System.currentTimeMillis();

                if (!finished) {
                    System.out.println("=== TIMEOUT DU PROCESSUS MAVEN ===");
                    process.destroyForcibly();
                    throw new RuntimeException("Maven process timeout after 20 minutes");
                }

                int exitCode = process.exitValue();
                System.out.println("Processus terminé avec code: " + exitCode);

                // Attendre que le thread d'erreur se termine
                try {
                    errorThread.join(5000); // Attendre max 5 secondes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Ajouter les erreurs aux logs
                if (errorLogs.length() > 0) {
                    logs.append("\n=== ERREURS MAVEN ===\n");
                    logs.append(errorLogs.toString());
                    System.out.println("Des erreurs Maven ont été détectées");
                }

                // Lire et analyser le rapport Cucumber
                System.out.println("=== LECTURE DU RAPPORT CUCUMBER ===");
                String jsonReport = readCucumberReport();
                System.out.println("Rapport JSON lu, taille: " + (jsonReport != null ? jsonReport.length() : "null"));

                // Calculer les métriques
                System.out.println("=== CALCUL DES MÉTRIQUES ===");
                int totalTests = countTotalTests(jsonReport);
                int passedTests = countPassedTests(jsonReport);
                int failedTests = countFailedTests(jsonReport);
                int skippedTests = totalTests - (passedTests + failedTests);
                double successRate = totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;

                System.out.println("Total tests: " + totalTests);
                System.out.println("Passed tests: " + passedTests);
                System.out.println("Failed tests: " + failedTests);
                System.out.println("Success rate: " + successRate + "%");

                // Remplir l'objet TestSuite
                testSuite.setLogs(logs.toString());
                testSuite.setJsonReport(jsonReport);
                testSuite.setExecutionTimeMs(endTime - startTime);
                testSuite.setTotalTests(totalTests);
                testSuite.setPassedTests(passedTests);
                testSuite.setFailedTests(failedTests);
                testSuite.setSkippedTests(skippedTests);
                testSuite.setSuccessRate(successRate);

                // Sauvegarder dans la base de données
                System.out.println("=== SAUVEGARDE EN BASE ===");
                TestSuite savedTestSuite = testSuiteRepository.save(testSuite);
                System.out.println("TestSuite sauvegardé avec ID: " + savedTestSuite.getId());
                System.out.println("=== FIN executeAllTests ===");
                return savedTestSuite;

            } catch (Exception e) {
                System.out.println("=== ERREUR executeAllTests ===");
                e.printStackTrace();
                testSuite.setLogs("Error executing tests: " + e.getMessage());
                TestSuite savedTestSuite = testSuiteRepository.save(testSuite);
                System.out.println("TestSuite d'erreur sauvegardé avec ID: " + savedTestSuite.getId());
                return savedTestSuite;
            }
        });
    }


    /**
     * Récupère le dernier test exécuté sans tag, sans utilisateur et sans mission
     * @return Le dernier TestSuite sans tag, utilisateur et mission, ou null si aucun n'existe
     */
    public TestSuite getLatestTestWithoutTagUserMission() {
        return testSuiteRepository.findLatestTestWithoutTagUserMission();
    }
}
