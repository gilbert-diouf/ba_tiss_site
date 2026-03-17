package com.example.batiss.music.GestionDesSons.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionRequest {

    private String transactionId;

    private String telephone;

    private double montant;
}
