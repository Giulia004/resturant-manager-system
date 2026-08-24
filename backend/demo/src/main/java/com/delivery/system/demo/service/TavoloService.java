package com.delivery.system.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.model.Tavolo;
import com.delivery.system.demo.repository.TavoloRepository;

@Service
public class TavoloService {
    private final TavoloRepository tavoloRepository;

    public TavoloService(TavoloRepository tavoloRepository) {
        this.tavoloRepository = tavoloRepository;
    }

    public List<Tavolo> getAllTavoli() {
        return this.tavoloRepository.findAll();
    }

    public Tavolo getTavoloById(Long id) {
        return tavoloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tavolo non trovato"));
    }
    
    @Transactional
    public Tavolo create(Tavolo tavolo) {
        return tavoloRepository.save(tavolo);
    }

    // Logica di business: verifica se è possibile occupare il tavolo o no
    public Tavolo isDisponibile(Long id) {
        Tavolo tavolo = tavoloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (tavolo.getDisponibile())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il tavolo è già occupato");

        tavolo.setDisponibile(true);
        return tavoloRepository.save(tavolo);
    }

    @Transactional
    public Tavolo update(Long id, Tavolo nuovoTavolo) {
        Tavolo tavolo = tavoloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Il tavolo non esiste"));

        tavolo.setNumero(nuovoTavolo.getNumero());
        tavolo.setPosti(nuovoTavolo.getPosti());
        tavolo.setDisponibile(nuovoTavolo.getDisponibile());

        return tavoloRepository.save(tavolo);
    }

    @Transactional
    public void delete(Long id) {
        if (!tavoloRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun tavolo trovato");
        tavoloRepository.deleteById(id);
    }
}
