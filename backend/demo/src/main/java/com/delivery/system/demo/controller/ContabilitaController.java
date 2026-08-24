package com.delivery.system.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.DocumentoFiscale;
import com.delivery.system.demo.service.ContabilitaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/contabilita")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasAnyRole('ADMIN','CASSIERE')")

public class ContabilitaController {
    @Autowired
    private ContabilitaService contabilitaService;

    @GetMapping("/documenti")
    public ResponseEntity<List<DocumentoFiscale>> getDocumenti(@RequestParam(defaultValue = "OGGI") String periodo) {
        List<DocumentoFiscale> documenti = contabilitaService.getDocumentiPerPeriodo(periodo);
        return ResponseEntity.ok(documenti);
    }

    // GET: Recupera il totale del fatturato per i KPI della dashboard
    @GetMapping("/fatturato")
    public ResponseEntity<Double> getFatturatoTotale(@RequestParam(defaultValue = "OGGI") String periodo) {
        Double fatturato = contabilitaService.getFatturatoTotale(periodo);
        return ResponseEntity.ok(fatturato);
    }

    // POST: Emette una nuova fattura o ricevuta
    @PostMapping("/fatture")
    public ResponseEntity<DocumentoFiscale> emettiFattura(@RequestBody DocumentoFiscale documentoFiscale) {
        try {
            DocumentoFiscale salvato = contabilitaService.emettiFattura(documentoFiscale);
            return ResponseEntity.ok(salvato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
