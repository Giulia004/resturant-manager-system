package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Piatto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PiattoRepository extends JpaRepository<Piatto,Long> {
    
}
