package com.resturant.demo.model;

import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "prodotto_id") // Usiamo il Prodotto generico che hai creato prima
    private Prodotto prodotto;

    private int quantity;

    private String notes; // Es: "Ben cotta", "+ Doppia Mozzarella"

    private double priceAtOrder; // Importante: salva il prezzo attuale del menù qui
}
