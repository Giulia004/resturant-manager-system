package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Tavolo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TavoloRepository extends JpaRepository<Tavolo,Long> {
    Optional<Tavolo> findByNumero(Integer numero);
}