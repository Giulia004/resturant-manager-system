package com.delivery.system.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.model.Piatto;
import com.delivery.system.demo.repository.PiattoRepository;

@Service
public class PiattoService {
    private final PiattoRepository piattoRepository;

    public PiattoService(PiattoRepository piattoRepository) {
        this.piattoRepository = piattoRepository;
    }

    public List<Piatto> getAllPiatti() {
        return piattoRepository.findAll();
    }

    public Piatto findPiattoById(Long id) {
        return piattoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Il piatto non esiste"));
    }

    @Transactional
    public Piatto create(Piatto piatto) {
        return piattoRepository.save(piatto);
    }

    @Transactional
    public Piatto update(Long id, Piatto piatto) {
        Piatto newPiatto = piattoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Il piatto non esiste"));

        newPiatto.setNome(piatto.getNome());
        newPiatto.setPrezzo(piatto.getPrezzo());
        newPiatto.setDescrizione(piatto.getDescrizione());
        newPiatto.setDisponibile(piatto.getDisponibile());
        newPiatto.setCategoria(piatto.getCategoria());

        return piattoRepository.save(newPiatto);
    }

    @Transactional
    public void delete(Long id) {
        if (!piattoRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Il piatto non esiste");

        piattoRepository.deleteById(id);
    }
}
