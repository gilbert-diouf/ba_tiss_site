package com.example.batiss.music.GestionDesSons.service;

import com.example.batiss.music.GestionDesSons.entity.Status;
import com.example.batiss.music.GestionDesSons.entity.Transaction;
import com.example.batiss.music.GestionDesSons.entity.User;
import com.example.batiss.music.GestionDesSons.repository.TransactionRepository;
import com.example.batiss.music.GestionDesSons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor  // ← remplace @AllArgsConstructor, ignore les @Value
public class PaiementService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final GoogleDriveService googleDriveService;

    @Value("${google.drive.file-id}")
    private String fileId;  // ← maintenant correctement injecté par Spring

    public Transaction initierTransaction(String telephone) {
        User user = userRepository.findByTelephone(telephone)
                .orElseGet(() -> {
                    User nouveau = new User();
                    nouveau.setTelephone(telephone);
                    nouveau.setUsername(telephone);
                    nouveau.setPassword(UUID.randomUUID().toString());
                    return userRepository.save(nouveau);
                });

        transactionRepository.findByUserTelephoneAndStatus(telephone, Status.VALIDE)
                .ifPresent(t -> {
                    if (!t.isTokenUtilise() && t.getTokenExpiration().isAfter(LocalDateTime.now())) {
                        throw new RuntimeException("Tu as déjà un accès actif !");
                    }
                });

        Transaction transaction = new Transaction();
        transaction.setMontant(2000);
        transaction.setStatus(Status.EN_ATTENTE);
        transaction.setDatePaiement(LocalDateTime.now());
        transaction.setUser(user);

        return transactionRepository.save(transaction);
    }

    public String confirmerPaiement(String telephone) {

        System.out.println("Téléphone reçu : '" + telephone + "'");
        transactionRepository.findAll().forEach(t ->
                System.out.println("Transaction en base - telephone: '" + t.getUser().getTelephone() + "' status: " + t.getStatus())
        );
        Transaction transaction = transactionRepository
                .findByUserTelephoneAndStatus(telephone, Status.EN_ATTENTE)
                .orElseThrow(() -> new RuntimeException("Aucune transaction en attente pour ce numéro"));

        boolean paiementOk = verifierPaiementWave(telephone);
        if (!paiementOk) {
            throw new RuntimeException("Paiement Wave non détecté");
        }

        String token = UUID.randomUUID().toString();
        transaction.setDownloadToken(token);
        transaction.setTokenExpiration(LocalDateTime.now().plusHours(24));
        transaction.setTokenUtilise(false);
        transaction.setStatus(Status.VALIDE);
        transactionRepository.save(transaction);

        return token;
    }

    public String obtenirLienTelechargement(String token) {
        Transaction transaction = transactionRepository.findByDownloadToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (transaction.isTokenUtilise()) {
            throw new RuntimeException("Ce lien a déjà été utilisé");
        }

        if (transaction.getTokenExpiration().isBefore(LocalDateTime.now())) {
            transaction.setStatus(Status.ECHOUE);
            transactionRepository.save(transaction);
            throw new RuntimeException("Lien expiré, refais un paiement");
        }

        transaction.setTokenUtilise(true);  // ← une seule fois
        transactionRepository.save(transaction);

        try {
            return googleDriveService.generateDownloadLink(fileId);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du lien Drive");
        }
    }

    private boolean verifierPaiementWave(String telephone) {
        // TODO: brancher l'API Wave ici
        return true;
    }
}