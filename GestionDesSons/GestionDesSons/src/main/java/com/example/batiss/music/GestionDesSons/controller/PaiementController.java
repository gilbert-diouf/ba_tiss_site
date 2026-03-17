package com.example.batiss.music.GestionDesSons.controller;

import com.example.batiss.music.GestionDesSons.DTO.TransactionRequest;
import com.example.batiss.music.GestionDesSons.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @Value("${app.wave-url}")
    private String waveUrl;

    /* Etape 1: Enregistrer le numero et initier la transaction*/

    @PostMapping("/commande/initier")
    public ResponseEntity<?> initier (@RequestBody TransactionRequest request){
        try {
            paiementService.initierTransaction(request.getTelephone());
            return ResponseEntity.ok(Map.of(
                    "message", "Commande crée, procedez au paiement Wave",
                    "lienWave", waveUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /* Etape 2: Confirmer le paiement*/

    @PostMapping("/commande/confirmer")
    public ResponseEntity<?> confirmer (@RequestBody TransactionRequest request){
        System.out.println("=== CONFIRMER ===");
        System.out.println("Request complet : " + request);
        System.out.println("Telephone : " + request.getTelephone());
        System.out.println("TransactionId : " + request.getTransactionId());
        try {
            String token= paiementService.confirmerPaiement(request.getTelephone());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Paiement confirmé ! Clique sur télécharger"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }

    /* Etape 3: Télécharger avec le token*/

    @GetMapping("/download/{token}")
    public ResponseEntity<?> telecharger ( @PathVariable String token){
        try {
            String lien= paiementService.obtenirLienTelechargement(token);
            return ResponseEntity.ok(Map.of("lien", lien));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
