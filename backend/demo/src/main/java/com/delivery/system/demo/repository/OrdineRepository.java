package com.delivery.system.demo.repository;

import com.delivery.system.demo.model.Ordini;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Ordini, Long> {

}
