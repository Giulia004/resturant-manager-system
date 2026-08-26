package com.restaurant.management.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.restaurant.management.model.StatoOrdine;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdiniResponse {
    private Long id;
    private Integer numeroTavolo;
    private LocalDateTime dataCreazione;
    private StatoOrdine stato;
    private Double totale;
    private Double sconto;
    private String metodoPagamento;
    private List<OrdineItemResponse> righe;
}
