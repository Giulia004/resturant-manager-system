package com.delivery.system.dto.request;

import com.delivery.system.demo.model.Ruolo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreaUtenteRequest(
    @NotBlank(message = "L'username è obbligatorio")
        @Size(min = 4, max = 30, message = "L'username deve avere tra 4 e 30 caratteri")
        String username,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "La password deve contenere almeno una lettera maiuscola e un numero")
        String password,

        @NotNull(message = "Il ruolo è obbligatorio")
        Ruolo ruolo) {
    
}
