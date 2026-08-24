package com.delivery.system.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pagamenti")
@Data
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ordine_id",nullable = false)
    private Ordine ordine;

    private BigDecimal importoTotale;
    private BigDecimal importoScontato;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private LocalDateTime dataPagamento;
    private String operatore; //Il cassiere
}
