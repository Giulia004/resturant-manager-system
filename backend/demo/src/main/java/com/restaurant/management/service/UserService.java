package com.restaurant.management.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.restaurant.management.model.Utente;
import com.restaurant.management.repository.UtenteRepository;
import com.restaurant.management.dto.request.CreaUtenteRequest;

@Service
public class UserService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utente> findAllUser() {
        return utenteRepository.findAll();
    }

    public Utente findByUserId(Long id) {
        return utenteRepository.findById(Objects.requireNonNull(id, "id"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));
    }

    @Transactional
    public Utente create(CreaUtenteRequest request) {
        if (utenteRepository.findByUsername(request.username()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username già esistente");
        Utente utente = new Utente();
        utente.setUsername(request.username());
        utente.setPassword(passwordEncoder.encode(request.password()));
        utente.setRuolo(request.ruolo());

        return utenteRepository.save(utente);
    }

    @Transactional
    public Utente update(Long id, Utente dati) {
        Utente utente = utenteRepository.findById(Objects.requireNonNull(id, "id"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        if (dati.getUsername() != null)
            utente.setUsername(dati.getUsername());
        if (dati.getRuolo() != null)
            utente.setRuolo(dati.getRuolo());

        return utenteRepository.save(utente);
    }

    @Transactional
    public void delete(Long id) {
        if (!utenteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato.");
        }
        utenteRepository.deleteById(id);
    }
}
