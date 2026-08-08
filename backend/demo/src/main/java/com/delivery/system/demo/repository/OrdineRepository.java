package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Ordini;
import com.delivery.system.demo.model.StatoOrdine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface OrdineRepository extends JpaRepository<Ordini, Long> {
    List<Ordini> findByStatoAndDataCreazione(StatoOrdine stato, LocalDateTime time);
}
