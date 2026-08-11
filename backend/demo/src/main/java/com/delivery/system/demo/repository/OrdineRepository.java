package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Ordini;
import com.delivery.system.demo.model.StatoOrdine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrdineRepository extends JpaRepository<Ordini, Long> {
    @Query("SELECT o FROM Ordini o WHERE o.stato = :stato AND CAST(o.dataCreazione AS LocalDate) = CURRENT_DATE")
    List<Ordini> findOrdiniPagatiOggi(@Param("stato") StatoOrdine stato);
}
