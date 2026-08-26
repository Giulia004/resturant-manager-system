package com.restaurant.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.service.OrdineService;
import com.restaurant.management.dto.request.CambioStatoRequest;
import com.restaurant.management.dto.request.OrdineRequest;
import com.restaurant.management.dto.response.OrdineItemResponse;
import com.restaurant.management.dto.response.OrdiniResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import com.restaurant.management.model.Ordine;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CUOCO')")
public class OrdiniController {
    private final OrdineService ordineService;

    public OrdiniController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @GetMapping
    public List<OrdiniResponse> getAll() {
        return ordineService.findAll().stream().map(this::toResponse).toList();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Ordine> create(@Valid @RequestBody OrdineRequest request) {
        Ordine salvaOrdine = ordineService.creaOrdine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvaOrdine);
    }

    @PutMapping("/{id}/stato")
    @Transactional
    public ResponseEntity<Ordine> cambioStato(@PathVariable Long id, @Valid @RequestBody CambioStatoRequest request) {
        Ordine ordineAggiornato = ordineService.cambioStato(id, request);
        return ResponseEntity.ok(ordineAggiornato);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUOCO')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Metodo mapper
    private OrdiniResponse toResponse(Ordine ordine) {
        List<OrdineItemResponse> righe = ordine.getRighe().stream()
                .map(item -> OrdineItemResponse.builder().nomePiatto(item.getPiatto().getNome()).qta(item.getQta())
                        .prezzoUnitario(item.getPrezzoUnitario()).build())
                .toList();

        return OrdiniResponse.builder().id(ordine.getId()).numeroTavolo(ordine.getNumeroTavolo())
                .dataCreazione(ordine.getDataCreazione()).stato(ordine.getStato()).totale(ordine.getTotale())
                .sconto(ordine.getSconto()).metodoPagamento(ordine.getMetodoPagamento()).righe(righe).build();
    }
}
