package com.delivery.system.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class Piatto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;

    private Double prezzo;
    private Boolean disponibile = true;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
}
