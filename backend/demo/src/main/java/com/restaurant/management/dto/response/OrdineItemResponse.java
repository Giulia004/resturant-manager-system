package com.restaurant.management.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdineItemResponse {
    private String nomePiatto;
    private Integer qta;
    private Double prezzoUnitario;
}
