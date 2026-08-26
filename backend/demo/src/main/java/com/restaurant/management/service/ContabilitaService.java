package com.delivery.system.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.delivery.system.demo.model.DocumentoFiscale;
import com.delivery.system.demo.model.TipoDocumento;
import com.delivery.system.demo.repository.DocumentoFiscaleRepository;

@Service
public class ContabilitaService {
    private final DocumentoFiscaleRepository documentoFiscaleRepository;

    public ContabilitaService(DocumentoFiscaleRepository documentoFiscaleRepository) {
        this.documentoFiscaleRepository = documentoFiscaleRepository;
    }

    public List<DocumentoFiscale> getDocumentiPerPeriodo(String periodo) {
        LocalDateTime inizio;
        LocalDateTime fine = LocalDateTime.now();

        switch (periodo.toUpperCase()) {
            case "OGGI":
                inizio = LocalDate.now().atStartOfDay();
                break;
            case "SETTIMANA":
                inizio = LocalDate.now().minusDays(7).atStartOfDay();
                break;
            case "MESE":
                inizio = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                break;
            default:
                inizio = LocalDate.now().atStartOfDay();
                break;
        }

        return documentoFiscaleRepository.findByDataEmissioneBetween(inizio, fine);
    }

    // Emette e salva un nuovo documento fiscale
    public DocumentoFiscale emettiFattura(DocumentoFiscale nuovoDoc) {
        int annoCorrente = LocalDate.now().getYear();

        LocalDateTime inizioAnno = LocalDate.of(annoCorrente, 1, 1).atStartOfDay();
        LocalDateTime fineAnno = LocalDateTime.now(); // Inizializzato correttamente

        // Sfruttiamo il metodo count del repository per ottenere il numero progressivo
        long countAnno = documentoFiscaleRepository.countByDataEmissioneBetween(inizioAnno, fineAnno) + 1;

        String numeroGenerato = String.format("FAT-%d/%03d", annoCorrente, countAnno);

        nuovoDoc.setNumeroDocumento(numeroGenerato);
        nuovoDoc.setDataEmissione(LocalDateTime.now());
        nuovoDoc.setTipo(TipoDocumento.FATTURA);

        return documentoFiscaleRepository.save(nuovoDoc);
    }

    // Calcola il fatturato totale in base al periodo
    public Double getFatturatoTotale(String periodo) {
        LocalDateTime inizio;
        LocalDateTime fine = LocalDateTime.now();

        switch (periodo.toUpperCase()) {
            case "OGGI":
                inizio = LocalDate.now().atStartOfDay();
                break;
            case "SETTIMANA":
                inizio = LocalDate.now().minusDays(7).atStartOfDay();
                break;
            case "MESE":
                inizio = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                break;
            default:
                inizio = LocalDate.now().atStartOfDay();
                break;
        }

        Double totale = documentoFiscaleRepository.sommaFatturatoPeriodo(inizio, fine);
        return totale != null ? totale : 0.0;
    }
}
