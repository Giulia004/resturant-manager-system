package com.restaurant.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.management.model.Utente;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
}
