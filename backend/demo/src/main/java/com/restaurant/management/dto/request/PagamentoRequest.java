package com.restaurant.management.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
                @NotNull(message="L'ID dell'ordine è obbligatorio")Long ordineId,
                @NotNull(message="L'importo totale è obbligatorio")BigDecimal importoTotale,
                @DecimalMin(value = "0.0", message = "Lo sconto non può essere negativo") BigDecimal importoScontato,
                @NotBlank(message = "Il metodo di pagamento è obbligatorio") String metodoPagamento) {

}
