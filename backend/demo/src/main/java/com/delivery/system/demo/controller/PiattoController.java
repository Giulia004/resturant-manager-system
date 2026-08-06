package com.delivery.system.demo.controller;

import com.delivery.system.demo.model.Piatto;
import com.delivery.system.demo.repository.PiattoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/piatti")
public class PiattoController {
    private final PiattoRepository repository;

    public PiattoController(PiattoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Piatto> getAll() {
        return repository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Piatto create(@RequestBody Piatto piatto) {
        return repository.save(piatto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piatto> getPiattoById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Modifica di un piatto
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Piatto> update(@PathVariable Long id, @RequestBody Piatto nuovoPiatto) {
        return repository.findById(id).map(piatto -> {
            piatto.setNome(nuovoPiatto.getNome());
            piatto.setPrezzo(nuovoPiatto.getPrezzo());
            piatto.setDescrizione(nuovoPiatto.getDescrizione());
            piatto.setDisponibile(nuovoPiatto.getDisponibile());
            piatto.setCategoria(nuovoPiatto.getCategoria());

            return ResponseEntity.ok(repository.save(piatto));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminazione di un piatto tramite l'id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id))
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}