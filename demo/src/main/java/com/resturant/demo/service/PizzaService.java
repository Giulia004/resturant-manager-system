package com.resturant.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resturant.demo.repository.PizzaRepository;
import com.resturant.demo.model.Pizza;

import java.util.List;

//CRUD operations
@Service
public class PizzaService {
    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    public Pizza createPizza(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    public Pizza updatePizza(Long id, Pizza pizza) {
        Pizza p = pizzaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pizza non trovata"));
        p.setName(pizza.getName());
        p.setPrice(pizza.getPrice());

        return pizzaRepository.save(p);
    }

    public void deletePizza(Long id) {
        pizzaRepository.deleteById(id);
    }
}
