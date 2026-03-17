package com.example.batiss.music.GestionDesSons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private double montant;

    private LocalDateTime datePaiement;

    // Token unique généré après paiement confirmé
    private String downloadToken;

    // Lien valable 24h
    private LocalDateTime tokenExpiration;

    // Token déjà utilisé ou non
    private boolean tokenUtilise = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
