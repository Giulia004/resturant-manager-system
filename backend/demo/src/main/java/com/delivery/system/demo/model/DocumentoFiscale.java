package com.delivery.system.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "documenti_fiscali")
@Getter
@Setter
public class DocumentoFiscale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique=true)
    private String numeroDocumento;

    @Column(nullable = false)
    private LocalDateTime dataEmissione;

    @Column(nullable = false)
    private String intestatario;

    @Column(nullable = false)
    private String partitaIvaCF;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;

    @Column(nullable = false)
    private Double totale;

    private Long ordineId;

}