package com.restaurant.management.dto.response;

import com.restaurant.management.model.Ruolo;
import com.restaurant.management.model.Utente;

public record UtenteResponse(Long id, String username,Ruolo ruolo) {
    public static UtenteResponse from(Utente utente) {
        return new UtenteResponse(utente.getId(), utente.getUsername(), utente.getRuolo());
    }
}
