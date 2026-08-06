package com.delivery.system.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.repository.OrdineRepository;
import com.delivery.system.demo.model.Ordini;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
@PreAuthorize("hasRole('ADMIN','CAMERIERE','CUOCO')")
public class OrdiniController {
    private final OrdineRepository repository;

    public OrdiniController(OrdineRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Ordini> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Ordini create(@RequestBody Ordini ordine) {
        return repository.save(ordine);
    }
}
