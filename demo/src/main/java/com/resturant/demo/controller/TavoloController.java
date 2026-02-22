package com.resturant.demo.controller;

import com.resturant.demo.model.Tavolo;
import com.resturant.demo.repository.TavoloRepository;
import com.resturant.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tavoli")
@CrossOrigin(origins = "http://localhost:3000")
public class TavoloController {

    private final TavoloRepository tavoloRepository;
    private final UserRepository userRepository;

    public TavoloController(TavoloRepository tavoloRepository, UserRepository userRepository) {
        this.tavoloRepository = tavoloRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Tavolo> getAllTavoli() {
        return tavoloRepository.findAll();
    }

    @PutMapping("/{id}/assegna/{userId}")
    public ResponseEntity<?> assegnaUtente(@PathVariable Long id, @PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("Utente non trovato");
        }

        return tavoloRepository.findById(id)
                .map(tavolo -> {
                    tavolo.setUtenteId(userId);
                    tavolo.setStatus("OCCUPATO");
                    return ResponseEntity.ok(tavoloRepository.save(tavolo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/libera")
    public ResponseEntity<Tavolo> liberaTavolo(@PathVariable Long id) {
        return tavoloRepository.findById(id)
                .map(tavolo -> {
                    tavolo.setUtenteId(null);
                    tavolo.setStatus("LIBERO");
                    return ResponseEntity.ok(tavoloRepository.save(tavolo));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}