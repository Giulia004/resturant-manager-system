package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Tavolo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TavoloRepository extends JpaRepository<Tavolo,Long> {
    
}