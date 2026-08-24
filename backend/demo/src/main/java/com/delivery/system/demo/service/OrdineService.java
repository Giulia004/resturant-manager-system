package com.delivery.system.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.model.Ordine;
import com.delivery.system.demo.model.OrdineItem;
import com.delivery.system.demo.model.Piatto;
import com.delivery.system.demo.model.StatoOrdine;
import com.delivery.system.demo.model.Tavolo;
import com.delivery.system.demo.repository.OrdineRepository;
import com.delivery.system.demo.repository.PiattoRepository;
import com.delivery.system.demo.repository.TavoloRepository;
import com.delivery.system.dto.request.CambioStatoRequest;
import com.delivery.system.dto.request.OrdineItemRequest;
import com.delivery.system.dto.request.OrdineRequest;

@Service
public class OrdineService {
    private final OrdineRepository ordineRepository;
    private final TavoloRepository tavoloRepository;
    private final PiattoRepository piattoRepository;

    public OrdineService(OrdineRepository ordineRepository, TavoloRepository tavoloRepository,
            PiattoRepository piattoRepository) {
        this.ordineRepository = ordineRepository;
        this.tavoloRepository = tavoloRepository;
        this.piattoRepository = piattoRepository;
    }

    public List<Ordine> findAll() {
        return ordineRepository.findAll();
    }

    @Transactional
    public Ordine creaOrdine(OrdineRequest request) {
        if (request.righe() == null || request.righe().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ordine deve contenere almeno un piatto.");

        Tavolo tavolo = tavoloRepository.findByNumero(request.numeroTavolo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tavolo numero " + request.numeroTavolo() + " non trovato."));

        Ordine ordine = new Ordine();
        ordine.setTavolo(tavolo);
        ordine.setNumeroTavolo(tavolo.getNumero());
        ordine.setDataCreazione(LocalDateTime.now());
        ordine.setStato(StatoOrdine.IN_ATTESA);

        if (ordine.getRighe() == null)
            ordine.setRighe(new ArrayList<>());

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

        return ordineRepository.save(ordine);
    }

    @Transactional
    public Ordine cambioStato(Long id, CambioStatoRequest request) {
        Ordine ordine = ordineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ordine non trovato"));
        ordine.setStato(request.stato());
        return ordineRepository.save(ordine);
    }

    @Transactional
    public void delete(Long id) {
        if(!ordineRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordine non trovato");

        ordineRepository.deleteById(id);
    }
}
