package com.delivery.system.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.model.MetodoPagamento;
import com.delivery.system.demo.model.Ordine;
import com.delivery.system.demo.model.Pagamento;
import com.delivery.system.demo.model.StatoOrdine;
import com.delivery.system.demo.repository.OrdineRepository;
import com.delivery.system.demo.repository.PagamentoRepository;
import com.delivery.system.dto.request.PagamentoRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final OrdineRepository ordineRepository;

    @Transactional
    public Ordine processaPagamento(Long ordineId, PagamentoRequest request) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordine non trovato"));

        if (ordine.getStato() == StatoOrdine.PAGATO)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ordine è già stato pagato");

        if (ordine.getStato() == StatoOrdine.ANNULLATO)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Questo ordine non può essere pagato");

        double totale = ordine.getTotale();
        BigDecimal sconto = request.importoScontato();

        if (sconto != null && sconto.doubleValue() > 0) {
            if (sconto.doubleValue() >= totale)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Lo sconto non può essere superiore o uguale al totale");
            ordine.setSconto(sconto.doubleValue());
            totale -= sconto.doubleValue();
        }

        ordine.setTotale(totale);
        ordine.setStato(StatoOrdine.PAGATO);
        ordine.setMetodoPagamento(request.metodoPagamento());

        Pagamento pagamento = new Pagamento();
        pagamento.setOrdine(ordine);
        pagamento.setImportoTotale(BigDecimal.valueOf(totale));
        pagamento.setImportoScontato(sconto);
        pagamento.setMetodoPagamento(MetodoPagamento.valueOf(request.metodoPagamento()));
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        return ordineRepository.save(ordine);
    }
}