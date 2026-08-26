package com.restaurant.management.controller;

import com.restaurant.management.model.Piatto;
import com.restaurant.management.service.PiattoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/piatti")
public class PiattoController {
    private final PiattoService piattoService;

    public PiattoController(PiattoService piattoService) {
        this.piattoService = piattoService;
    }

    @GetMapping
    public List<Piatto> getAll() {
        return piattoService.getAllPiatti();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Piatto> create(@Valid @RequestBody Piatto piatto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(piattoService.create(piatto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piatto> getPiattoById(@PathVariable Long id) {
        return ResponseEntity.ok(piattoService.findPiattoById(id));
    }

    // Modifica di un piatto
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Piatto> update(@PathVariable Long id, @Valid @RequestBody Piatto nuovoPiatto) {
        return ResponseEntity.ok(piattoService.update(id, nuovoPiatto));
    }

    // Eliminazione di un piatto tramite l'id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        piattoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}