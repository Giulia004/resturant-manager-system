package com.restaurant.management.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.model.Utente;
import com.restaurant.management.repository.UtenteRepository;
import com.restaurant.management.security.JwtUtil;
import com.restaurant.management.security.LoginRateLimiter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authMnager;
    private final JwtUtil jwtUtil;
    private final UtenteRepository utenteRepository;
    private final LoginRateLimiter rateLimiter;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UtenteRepository utenteRepository, LoginRateLimiter rateLimiter) {
        this.authMnager = authManager;
        this.jwtUtil = jwtUtil;
        this.utenteRepository = utenteRepository;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username e password sono obbligatori"));
        }

        rateLimiter.verificaBlocco(username);

        try{
            authMnager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            rateLimiter.registraFallimento(username);
            throw ex;
        }

        rateLimiter.registraSuccesso(username);

        Utente utente = utenteRepository.findByUsername(username).orElseThrow();
        String token = jwtUtil.generateToken(utente.getUsername(), utente.getRuolo().name());

        return ResponseEntity.ok(Map.of("token", token, "ruolo", utente.getRuolo()));
    }
}
