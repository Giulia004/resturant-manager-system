package com.restaurant.management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.model.Utente;
import com.restaurant.management.service.UserService;
import com.restaurant.management.dto.request.CreaUtenteRequest;
import com.restaurant.management.dto.response.UtenteResponse;

import jakarta.validation.Valid;

import java.util.List;

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
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UtenteResponse> getAll() {
        return userService.findAllUser().stream().map((UtenteResponse::from)).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtenteResponse> getById(@PathVariable Long id) {
        Utente utente = userService.findByUserId(id);
        return ResponseEntity.ok(UtenteResponse.from(utente));
    }

    @PostMapping
    public ResponseEntity<UtenteResponse> create(@Valid @RequestBody CreaUtenteRequest request) {
        Utente nuovoUtente = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UtenteResponse.from(nuovoUtente));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtenteResponse> update(@PathVariable Long id, @RequestBody Utente dati) {
        Utente userUpdate = userService.update(id, dati);
        return ResponseEntity.ok(UtenteResponse.from(userUpdate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}