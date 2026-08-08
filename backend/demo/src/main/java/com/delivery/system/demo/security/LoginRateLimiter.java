package com.delivery.system.demo.security;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.delivery.system.demo.exception.BlockedAccountException;

/**
 * Rate limiter in-memory (per singola istanza) contro il brute-force sul login.
 * Dopo MAX_TENTATIVI fallimenti consecutivi per la stessa chiave (username),
 * blocca ulteriori tentativi per BLOCCO_MINUTI minuti.
 *
 * Nota: essendo in memoria, il contatore si azzera al riavvio dell'app e non e'
 * condiviso tra piu' istanze. Per un deployment multi-istanza usare Redis o
 * simili.
 */

@Component
public class LoginRateLimiter {
    private static final int MAX_TENATIVI = 5;
    private static final long BLOCCO_MINUTI = 15;

    private record Tentativo(int conteggio, Instant bloccaFino) {
    }
    
    private final ConcurrentHashMap<String, Tentativo> tentativi = new ConcurrentHashMap<>();

    public void verificaBlocco(String chiave) {
        Tentativo t = tentativi.get(chiave.toLowerCase());
        if (t != null && t.bloccaFino() != null && Instant.now().isBefore(t.bloccaFino())) {
            throw new BlockedAccountException("Troppi tentativi falliti. Ripriva tra qualche minuto");
        }
    }

    public void registraFallimento(String chiave) {
        tentativi.compute(chiave.toLowerCase(), (k, v) -> {
            int conteggio = (v == null ? 0 : v.conteggio()) + 1;
            Instant bloccaFino = conteggio >= MAX_TENATIVI ? Instant.now().plusSeconds(BLOCCO_MINUTI * 60) : null;
            return new Tentativo(conteggio, bloccaFino);
        });
    }
    
    public void registraSuccesso(String chiave) {
        tentativi.remove(chiave.toLowerCase());
    }
}
