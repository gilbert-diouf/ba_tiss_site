package com.example.batiss.music.GestionDesSons.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 29)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(length = 25)
    private String email;

    // Numéro Wave de l'acheteur (+221XXXXXXXXX)
    @Column(unique = true, length = 15)
    private String telephone;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
