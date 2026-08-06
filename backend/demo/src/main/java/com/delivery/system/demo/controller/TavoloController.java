package com.delivery.system.demo.controller;

import com.delivery.system.demo.model.Tavolo;
import com.delivery.system.demo.repository.TavoloRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tavoli")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CASSIERE')")
public class TavoloController {
    private final TavoloRepository repository;

    public TavoloController(TavoloRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tavolo> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Tavolo create(@RequestBody Tavolo tavolo) {
        return repository.save(tavolo);
    }

    // Recupero del tavolo tramite il suo id
    @GetMapping("/{id}")
    public ResponseEntity<Tavolo> getTavoloById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tavolo> update(@PathVariable Long id,
            @RequestBody Tavolo nuovoTavolo) {

        return repository.findById(id)
                .map(tavolo -> {
                    tavolo.setNumero(nuovoTavolo.getNumero());
                    tavolo.setPosti(nuovoTavolo.getPosti());
                    tavolo.setDisponibile(nuovoTavolo.getDisponibile());

                    return ResponseEntity.ok(repository.save(tavolo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id))
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}