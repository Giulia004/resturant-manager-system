package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.OrdineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineItemRepository extends JpaRepository<OrdineItem, Long> {
    
}
