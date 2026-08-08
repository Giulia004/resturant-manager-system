package com.delivery.system.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.OrdineItem;
import com.delivery.system.demo.repository.OrdineItemRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordine-items")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CUOCO')")
public class OrdineItemController {
    private final OrdineItemRepository repository;

    public OrdineItemController(OrdineItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<OrdineItem> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdineItem> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrdineItem create(@RequestBody OrdineItem ordineItem) {
        return repository.save(ordineItem);
    }
}
