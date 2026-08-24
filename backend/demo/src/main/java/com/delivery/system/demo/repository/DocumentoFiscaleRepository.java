package com.delivery.system.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delivery.system.demo.model.DocumentoFiscale;

public interface DocumentoFiscaleRepository extends JpaRepository<DocumentoFiscale, Long> {
    // Trova tutti i documenti emessi in un determinato intervallo di tempo
    List<DocumentoFiscale> findByDataEmissioneBetween(LocalDateTime inizio, LocalDateTime fine);

    @Query("SELECT SUM(d.totale) FROM DocumentoFiscale d WHERE d.dataEmissione BETWEEN :inizio AND :fine")
    Double sommaFatturatoPeriodo(@Param("inizio") LocalDateTime inizio, @Param("fine") LocalDateTime fine);

    long countByDataEmissioneBetween(LocalDateTime inizio, LocalDateTime fine);
}
