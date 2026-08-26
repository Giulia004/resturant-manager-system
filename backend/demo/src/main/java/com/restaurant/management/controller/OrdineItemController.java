package com.restaurant.management.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.model.OrdineItem;
import com.restaurant.management.service.OrdineItemService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordine-items")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CUOCO')")
public class OrdineItemController {
    private final OrdineItemService ordineItemService;

    public OrdineItemController(OrdineItemService ordineItemService) {
        this.ordineItemService = ordineItemService;
    }

    @GetMapping
    public List<OrdineItem> getAll() {
        return ordineItemService.findAllItems();
    }

    @GetMapping("/{id}")
    public OrdineItem getById(@PathVariable Long id) {
        return ordineItemService.findById(id);
    }

    @PostMapping
    public OrdineItem create(@RequestBody OrdineItem ordineItem) {
        return ordineItemService.save(ordineItem);
    }
}
