package com.resturant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.resturant.demo.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza,Long>{
    
}
