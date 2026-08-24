package com.delivery.system.demo.controller;

import com.delivery.system.demo.model.Tavolo;
import com.delivery.system.demo.service.TavoloService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tavoli")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CASSIERE')")
public class TavoloController {
    private final TavoloService tavoloService;

    public TavoloController(TavoloService tavoloService) {
        this.tavoloService = tavoloService;
    }
    
    @GetMapping
    public List<Tavolo> getAll() {
        return tavoloService.getAllTavoli();
    }

    @PostMapping
    public Tavolo create(@RequestBody Tavolo tavolo) {
        return tavoloService.create(tavolo);
    }

    // Recupero del tavolo tramite il suo id
    @GetMapping("/{id}")
    public ResponseEntity<Tavolo> getTavoloById(@PathVariable Long id) {
        return ResponseEntity.ok(tavoloService.getTavoloById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tavolo> update(@PathVariable Long id,
            @RequestBody Tavolo nuovoTavolo) {

        return ResponseEntity.ok(tavoloService.update(id, nuovoTavolo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tavoloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}