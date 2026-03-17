package com.example.batiss.music.GestionDesSons.repository;

import com.example.batiss.music.GestionDesSons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Override
    Optional<User> findById(Long aLong);
    Optional<User> findByTelephone(String telephone);

    Optional<User> findByUsername(String username);
}
