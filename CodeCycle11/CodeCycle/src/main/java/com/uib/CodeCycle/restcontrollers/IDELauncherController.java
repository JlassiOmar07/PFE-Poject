package com.uib.CodeCycle.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ide")
@CrossOrigin(origins = "*")
public class IDELauncherController {

    @PostMapping("/launch")
    public ResponseEntity<String> launchIDE(@RequestBody(required = false) Map<String, Object> payload) {
        String idePath = "C:\\workspace\\testautomation\\webDriver_java\\UibTestProjectJUNIT";
        
        try {
            // Commande pour ouvrir l'IDE (supposant que vous utilisez IntelliJ IDEA)
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "idea64.exe", idePath);
            processBuilder.start();
            
            return ResponseEntity.ok("IDE lancé avec succès");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors du lancement de l'IDE: " + e.getMessage());
        }
    }
}