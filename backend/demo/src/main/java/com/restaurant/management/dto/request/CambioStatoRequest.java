package com.restaurant.management.dto.request;

import com.restaurant.management.model.StatoOrdine;

import jakarta.validation.constraints.NotNull;

public record CambioStatoRequest(
    @NotNull(message = "Lo stato è obbligatorio")
    StatoOrdine stato) {
    
}
