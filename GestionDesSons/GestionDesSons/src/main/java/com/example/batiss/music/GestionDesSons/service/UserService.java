package com.example.batiss.music.GestionDesSons.service;

import com.example.batiss.music.GestionDesSons.entity.User;
import com.example.batiss.music.GestionDesSons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {  // ← ajouté

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ← pour encoder le password

    // ✅ Méthode requise par Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    // ✅ Tes méthodes existantes — inchangées
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) throws ApiError {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ApiError(404, "Le nom est obligatoire");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ApiError(404, "Le mot de passe est obligatoire");
        }

        // ✅ Encoder le password avant de sauvegarder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }
}