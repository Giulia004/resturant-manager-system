package com.resturant.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.resturant.demo.model.Prodotto;
import com.resturant.demo.model.Categoria;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByCategoria(Categoria categoria);// Find by category

    List<Prodotto> findByDisponibileTrue();// Trova per disponibilità

    List<Prodotto> findByCategoriaAndDisponibileTrue(Categoria categoria);

    List<Prodotto> findByNomeContaining(String nome);

}
