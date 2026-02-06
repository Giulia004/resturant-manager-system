package com.resturant.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User cameriere; /*Chi ha preso l'ordine */

    @ManyToOne
    private Tavolo tavolo; /*A quale tavolo è associato l'ordine */
    
    private LocalDateTime date = LocalDateTime.now();
    
    private double totalPrice;

    @Column(nullable = false)
    private String status; /*es: "In preparazione", "Pronto", "Consegnato" */

    @ManyToMany
    @JoinTable(name = "order_pizza",
        joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private List<Pizza> pizzas;
}
