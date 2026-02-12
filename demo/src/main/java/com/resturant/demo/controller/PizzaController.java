package com.resturant.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resturant.demo.repository.PizzaRepository;

import com.resturant.demo.model.Pizza;
import java.util.List;

@RestController
@RequestMapping("/api/pizze")
@CrossOrigin(origins = "http://localhost:5175")

public class PizzaController {
    private final PizzaRepository pizzaRepo;

    public PizzaController(PizzaRepository pizzaRepo) {
        this.pizzaRepo = pizzaRepo;
    }

    @GetMapping
    public List<Pizza> getAllPizzas() {
        return pizzaRepo.findAll();
    }

    //Map Pizza per id
    @GetMapping("/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
        return pizzaRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Add pizza
    @PostMapping
    public Pizza createPizza(@RequestBody Pizza pizza) {
        return pizzaRepo.save(pizza);
    }

    //Update Pizza
    @PutMapping("/{id}")
    public ResponseEntity<Pizza> updatePizza(@PathVariable Long id, @RequestBody Pizza pizzaDetails) {
        return pizzaRepo.findById(id).map(pizza -> {
            pizza.setName(pizzaDetails.getName());
            pizza.setIngredients(pizzaDetails.getIngredients());
            pizza.setPrice(pizzaDetails.getPrice());
            Pizza updatedPizza = pizzaRepo.save(pizza);
            return ResponseEntity.ok(updatedPizza);
        }).orElse(ResponseEntity.notFound().build());
    }

    //Delete Pizza
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        return pizzaRepo.findById(id).map(pizza -> {
            pizzaRepo.delete(pizza);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
