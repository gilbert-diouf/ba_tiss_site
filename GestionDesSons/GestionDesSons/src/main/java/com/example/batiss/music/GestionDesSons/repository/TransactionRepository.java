package com.example.batiss.music.GestionDesSons.repository;

import com.example.batiss.music.GestionDesSons.entity.Status;
import com.example.batiss.music.GestionDesSons.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Override
    Optional<Transaction> findById(Long aLong);
    Optional<Transaction> findByUserTelephoneAndStatus(String telephone, Status status);
    Optional<Transaction> findByDownloadToken(String token);
}
