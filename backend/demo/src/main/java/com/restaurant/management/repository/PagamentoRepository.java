package com.restaurant.management.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.restaurant.management.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    // Trova tutti i pagamenti effettuati in un determinato intervallo di tempo
    List<Pagamento> findByDataPagamentoBetween(LocalDateTime start, LocalDateTime end);

    Pagamento findByOrdineId(Long ordineId);
}
