package com.resturant.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prodotti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descrizione;

    @Column(nullable = false)
    private Double prezzo;

    @Column(nullable = false)
    private Boolean disponibile = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;
}