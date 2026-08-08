package com.delivery.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PagamentoRequest(
        @NotBlank(message = "Il metodo di pagamento è obbligatorio") String metodoPagamento,
        @Min(value = 0, message = "Lo sconto non può essere negativo") Double sconto) {

}
