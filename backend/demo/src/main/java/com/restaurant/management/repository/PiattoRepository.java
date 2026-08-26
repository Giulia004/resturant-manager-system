package com.restaurant.management.repository;

import com.restaurant.management.model.Piatto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PiattoRepository extends JpaRepository<Piatto,Long> {
    
}
