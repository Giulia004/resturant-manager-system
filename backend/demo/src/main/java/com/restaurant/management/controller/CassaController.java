package com.restaurant.management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.repository.OrdineRepository;
import com.restaurant.management.model.StatoOrdine;
import com.restaurant.management.model.Ordine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/cassa")
  @PreAuthorize("hasAnyRole('CASSIERE','ADMIN')")
public class CassaController {
    private final OrdineRepository repository;

    public CassaController(OrdineRepository repository) {
        this.repository = repository;
    }

    //Report Giornaliero
    @GetMapping("/report-giornaliero")
    public Map<String, Double> getReportGiornaliero() {
        List<Ordine> todayOrders = repository.findOrdiniPagatiOggi(StatoOrdine.PAGATO);

        return todayOrders.stream().collect(
                Collectors.groupingBy(o->o.getMetodoPagamento()!=null ? o.getMetodoPagamento(): "N/D", Collectors.summingDouble(Ordine::getTotale)));
    }
    
}
