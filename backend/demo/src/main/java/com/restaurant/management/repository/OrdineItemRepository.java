package com.restaurant.management.repository;

import com.restaurant.management.model.OrdineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineItemRepository extends JpaRepository<OrdineItem, Long> {
    
}
