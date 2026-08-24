package com.delivery.system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrdineItemRequest(
    @NotNull(message = "L'ID del piatto è obbligatorio")
        Long piattoId,
        @NotNull(message = "La quantità è obbligatoria")
        @Min(value=1,message = "La quantità deve essere almeno 1")
        Integer qta) {
    
}
