package com.delivery.system.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.Utente;
import com.delivery.system.demo.repository.UtenteRepository;
import com.delivery.system.dto.CreaUtenteRequest;
import com.delivery.system.dto.UtenteResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UtenteRepository repository;

    public UserController(UtenteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UtenteResponse> getAll() {
        return repository.findAll().stream().map(UtenteResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtenteResponse> getById(@PathVariable Long id) {
        return repository.findById(id).map(UtenteResponse::from).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreaUtenteRequest request){
        if(repository.findByUsername(request.username()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username già esistente"));

        Utente utente = new Utente();
        utente.setUsername(request.username());
        utente.setPassword(passwordEncoder.encode(request.password()));
        utente.setRuolo(request.ruolo());

        return ResponseEntity.status(HttpStatus.CREATED).body(UtenteResponse.from(repository.save(utente)));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtenteResponse> update(@PathVariable Long id, @RequestBody Utente dati) {
        return repository.findById(id).map(utente -> {
            if (dati.getUsername() != null)
                utente.setUsername(dati.getUsername());
            if (dati.getRuolo() != null)
                utente.setRuolo(dati.getRuolo());

            Utente updateUtente = repository.save(utente);
            return ResponseEntity.ok(UtenteResponse.from(updateUtente));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
