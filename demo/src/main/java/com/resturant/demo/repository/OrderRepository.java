package com.resturant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resturant.demo.model.Order;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByTavoloId(Long tavoloId);
    List<Order> findByCameriereId(Long cameriereId);
}
