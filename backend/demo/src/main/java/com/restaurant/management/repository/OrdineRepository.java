package com.restaurant.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrdineRepository extends JpaRepository<com.restaurant.management.model.Ordine, Long> {
    @Query("SELECT o FROM Ordine o WHERE o.stato = :stato AND CAST(o.dataCreazione AS LocalDate) = CURRENT_DATE")
    List<com.restaurant.management.model.Ordine> findOrdiniPagatiOggi(@Param("stato") com.restaurant.management.model.StatoOrdine stato);
}
