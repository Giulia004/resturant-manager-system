package com.delivery.system.dto;

import com.delivery.system.demo.model.Ruolo;
import com.delivery.system.demo.model.Utente;

public record UtenteResponse(Long id, String username,Ruolo ruolo) {
    public static UtenteResponse from(Utente utente) {
        return new UtenteResponse(utente.getId(), utente.getUsername(), utente.getRuolo());
    }
}
