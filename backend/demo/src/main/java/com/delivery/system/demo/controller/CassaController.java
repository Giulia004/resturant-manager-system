package com.delivery.system.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.Ordini;
import com.delivery.system.demo.model.StatoOrdine;
import com.delivery.system.demo.repository.OrdineRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/cassa")
public class CassaController {
    private final OrdineRepository repository;

    public CassaController(OrdineRepository repository) {
        this.repository = repository;
    }

    //Report Giornaliero
    @GetMapping("/report-giornaliero")
    public Map<String, Double> getReportGiornaliero() {
        LocalDateTime inizioGiornata = LocalDateTime.now().withHour(0).withMinute(0);
        List<Ordini> todayOrders = repository.findByStatoAndDataCreazione(StatoOrdine.PAGATO, inizioGiornata);

        return todayOrders.stream().collect(
                Collectors.groupingBy(o->o.getMetodoPagamento()!=null ? o.getMetodoPagamento(): "N/D", Collectors.summingDouble(Ordini::getTotale)));
    }
    
}
