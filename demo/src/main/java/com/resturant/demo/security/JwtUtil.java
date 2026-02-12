package com.resturant.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Nota: Assicurati che la chiave sia sufficientemente lunga per HS256
    private static final String SECRET_KEY = "bXlTdXBlclNlY3JldEtlbXlTdXBlclNlY3JldEtlbXlTdXBlclNlY3JldEtl";
    
    // In 0.12.0+ si usa SecretKey invece di Key per chiarezza con HMAC
    private final SecretKey key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET_KEY));
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 ora

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key) // L'algoritmo viene dedotto automaticamente dalla chiave
                .compact();
    }

    // Estrae il "subject" (username) dal token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Metodo generico per estrarre qualsiasi claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token) {
        try {
            // Se il parsing va a buon fine e non lancia eccezioni, il token è valido
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Il token è scaduto, manomesso o malformato
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload(); // In 0.12+ getBody() è deprecato in favore di getPayload()
    }
}