package com.delivery.system.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrdineRequest(
        @NotNull(message = "Il numero del tavolo è obbligatorio")
    @Positive(message = "Il numero del tavolo deve essere positivo")
        Integer numeroTavolo,
            @NotEmpty(message = "L'ordine deve contenere almeno un articolo")
            List<OrdineItemRequest> righe) {
    
}
