package com.restaurant.management.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "tavolo")
@Getter
@Setter
public class Tavolo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Integer numero;
    
    private Integer posti;
    private Boolean disponibile = true;

    @Version
    private Long version;

    @OneToMany(mappedBy = "tavolo",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Ordine> ordini=new ArrayList<>();
}