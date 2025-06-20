package com.uib.CodeCycle.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin(origins = "*")
public class TestEventController {

    // Liste thread-safe pour stocker les émetteurs SSE actifs
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Endpoint pour s'abonner aux événements SSE
     * @return Un émetteur SSE configuré
     */
    @GetMapping(path = "/test-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        // Créer un émetteur avec un timeout très long
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // Configurer les callbacks pour nettoyer l'émetteur quand la connexion se termine
        emitter.onCompletion(() -> {
            System.out.println("SSE connection completed");
            emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            System.out.println("SSE connection timeout");
            emitters.remove(emitter);
        });
        emitter.onError(e -> {
            System.out.println("SSE connection error: " + e.getMessage());
            emitters.remove(emitter);
        });
        
        // Ajouter l'émetteur à la liste
        emitters.add(emitter);
        
        // Envoyer un événement initial pour confirmer la connexion
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected to test events stream"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }
        
        return emitter;
    }

    /**
     * Méthode pour envoyer une mise à jour d'étape de test à tous les clients connectés
     * @param testName Nom du test en cours d'exécution
     * @param stepLine Ligne de l'étape Cucumber
     */
    public void sendTestStepUpdate(String testName, String stepLine) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        // Pour chaque émetteur actif, envoyer l'événement
        emitters.forEach(emitter -> {
            try {
                // Créer un événement nommé "test-step" avec les données
                emitter.send(SseEmitter.event()
                        .name("test-step")
                        .data(new TestStepEvent(testName, stepLine, System.currentTimeMillis())));
            } catch (IOException e) {
                // Si l'envoi échoue, marquer l'émetteur pour suppression
                deadEmitters.add(emitter);
            }
        });
        
        // Nettoyer les émetteurs morts
        emitters.removeAll(deadEmitters);
    }
    
    /**
     * Classe interne pour représenter un événement d'étape de test
     */
    private static class TestStepEvent {
        private final String testName;
        private final String step;
        private final long timestamp;
        
        public TestStepEvent(String testName, String step, long timestamp) {
            this.testName = testName;
            this.step = step;
            this.timestamp = timestamp;
        }
        
        // Getters nécessaires pour la sérialisation JSON
        public String getTestName() { return testName; }
        public String getStep() { return step; }
        public long getTimestamp() { return timestamp; }
    }
}