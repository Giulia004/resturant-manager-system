package com.resturant.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.resturant.demo.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import com.resturant.demo.model.Order;
import com.resturant.demo.service.OrderService;
import com.resturant.demo.dto.OrderRequest;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor // Uso Lombok, o aggiungi il costruttore manualmente per entrambi i field
public class OrderController {

    private final OrderRepository orderRepo;
    private final OrderService orderService; // Iniettiamo il service per la creazione complessa

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cambiamo il @PostMapping per usare il Record DTO e il Service
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        // Usiamo il service che calcola prezzi, gestisce item multipli e note
        Order nuovoOrdine = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoOrdine);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return orderRepo.findById(id).map(order -> {
            order.setStatus(status);
            return ResponseEntity.ok(orderRepo.save(order));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.modifyOrder(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}