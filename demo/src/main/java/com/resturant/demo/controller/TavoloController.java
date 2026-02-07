package com.resturant.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.resturant.demo.repository.TavoloRepository;
import com.resturant.demo.model.Tavolo;
import java.util.List;

@RestController
@RequestMapping("/api/tavoli")
@CrossOrigin(origins = "http://localhost:3000")
public class TavoloController {
    private final TavoloRepository tavoloRepo;

    public TavoloController(TavoloRepository tavoloRepo) {
        this.tavoloRepo = tavoloRepo;
    }

    @GetMapping
    public List<Tavolo> getAllTables() {
        return tavoloRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tavolo> getTableById(@PathVariable Long id) {
        return tavoloRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tavolo createTavolo(@RequestBody Tavolo tavolo) {
        return tavoloRepo.save(tavolo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tavolo> updateTavolo(@PathVariable Long id, @RequestBody Tavolo tavoloDetails) {
        return tavoloRepo.findById(id).map(tavolo -> {
            tavolo.setNumero(tavoloDetails.getNumero());
            tavolo.setPosti(tavoloDetails.getPosti());
            Tavolo updatedTavolo = tavoloRepo.save(tavolo);
            return ResponseEntity.ok(updatedTavolo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTavolo(@PathVariable Long id) {
        return tavoloRepo.findById(id).map(tavolo -> {
            tavoloRepo.delete(tavolo);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
