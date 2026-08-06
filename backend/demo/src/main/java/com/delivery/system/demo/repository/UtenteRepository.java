package com.delivery.system.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery.system.demo.model.Utente;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
}
