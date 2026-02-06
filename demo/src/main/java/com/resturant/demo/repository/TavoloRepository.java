package com.resturant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.resturant.demo.model.Tavolo;

public interface TavoloRepository extends JpaRepository<Tavolo,Long>{
    
}
