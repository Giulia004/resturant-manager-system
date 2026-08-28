package com.restaurant.management.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.restaurant.management.model.OrdineItem;
import com.restaurant.management.repository.OrdineItemRepository;

@Service
public class OrdineItemService {
    private final OrdineItemRepository ordineItemRepository;

    public OrdineItemService(OrdineItemRepository ordineItemRepository) {
        this.ordineItemRepository = ordineItemRepository;
    }

    public List<OrdineItem> findAllItems() {
        return ordineItemRepository.findAll();
    }

    public OrdineItem findById(Long id) {
        return ordineItemRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessuna corrispondenza trovata"));
    }
    
    @Transactional
    public OrdineItem save(OrdineItem ordineItem) {
        return ordineItemRepository.save(ordineItem);
    }
}