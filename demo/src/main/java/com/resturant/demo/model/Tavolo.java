package com.resturant.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tavoli")
public class Tavolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable = false)
    private int numero;

    @Column(nullable = false)
    private int posti;

    @Column(nullable =false)
    public String status;
}
