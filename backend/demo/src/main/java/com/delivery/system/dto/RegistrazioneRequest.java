package com.delivery.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrazioneRequest(
    @NotBlank(message = "L'username è obbligatorio")
    @Size(min=4, max = 30, message = "L'username deve avere tra i 4 e i 30 caratteri")
        String username,
            @NotBlank(message = "La password è obbligatoria")
            @Size(min=8,message = "La password deve contenere almeno 8 caratteri")
            @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "La password deve contenere almeno una lettera maiuscola e un numero")
        String password
) {
    
}
