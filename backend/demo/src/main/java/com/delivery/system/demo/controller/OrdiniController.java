package com.delivery.system.demo.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.repository.OrdineRepository;
import com.delivery.system.demo.repository.PiattoRepository;
import com.delivery.system.demo.repository.TavoloRepository;
import com.delivery.system.dto.CambioStatoRequest;
import com.delivery.system.dto.OrdineItemRequest;
import com.delivery.system.dto.OrdineRequest;

import jakarta.transaction.Transactional;

import com.delivery.system.demo.model.OrdineItem;
import com.delivery.system.demo.model.Ordini;
import com.delivery.system.demo.model.Piatto;
import com.delivery.system.demo.model.StatoOrdine;
import com.delivery.system.demo.model.Tavolo;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
@PreAuthorize("hasAnyRole('ADMIN','CAMERIERE','CUOCO')")
public class OrdiniController {
    private final OrdineRepository repository;
    private final TavoloRepository tavoloRepository;
    private final PiattoRepository piattoRepository;

    public OrdiniController(OrdineRepository repository, TavoloRepository tavoloRepository,
            PiattoRepository piattoRepository) {
        this.repository = repository;
        this.tavoloRepository = tavoloRepository;
        this.piattoRepository = piattoRepository;
    }

    @GetMapping
    public List<Ordini> getAll() {
        return repository.findAll();
    }

    @PostMapping
    @Transactional
    public Ordini create(@RequestBody OrdineRequest request) {
        if (request.righe() == null || request.righe().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ordine deve contenere almeno un piatto.");

        Tavolo tavolo = tavoloRepository.findByNumero(request.numeroTavolo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tavolo numero " + request.numeroTavolo() + " non trovato."));

        Ordini ordine = new Ordini();
        ordine.setTavolo(tavolo);
        ordine.setDataCreazione(LocalDateTime.now());
        ordine.setStato(StatoOrdine.IN_ATTESA);

        double totale = 0.0;

        for (OrdineItemRequest riga : request.righe()) {
            Piatto piatto = piattoRepository.findById(riga.piattoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Piatto " + riga.piattoId() + " non trovato."));

            OrdineItem item = new OrdineItem();
            item.setOrdine(ordine);
            item.setPiatto(piatto);
            item.setQta(riga.qta());
            item.setPrezzoUnitario(piatto.getPrezzo());

            totale += piatto.getPrezzo() * riga.qta();
            ordine.getRighe().add(item);
        }

        ordine.setTotale(totale);

        return repository.save(ordine);
    }

    @PutMapping("/{id}/stato")
    public ResponseEntity<Ordini> cambioStato(@PathVariable Long id, @RequestBody CambioStatoRequest request) {
        return repository.findById(id).map(ordine -> {
            ordine.setStato(request.stato());
            return ResponseEntity.ok(repository.save(ordine));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUOCO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
