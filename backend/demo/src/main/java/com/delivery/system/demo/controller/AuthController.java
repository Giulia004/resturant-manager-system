package com.delivery.system.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.Utente;
import com.delivery.system.demo.repository.UtenteRepository;
import com.delivery.system.demo.security.JwtUtil;
import com.delivery.system.demo.security.LoginRateLimiter;
import com.delivery.system.dto.RegistrazioneRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authMnager;
    private final JwtUtil jwtUtil;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginRateLimiter rateLimiter;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UtenteRepository utenteRepository,
            PasswordEncoder passwordEncoder, LoginRateLimiter rateLimiter) {
        this.authMnager = authManager;
        this.jwtUtil = jwtUtil;
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrazioneRequest request) {
        if (utenteRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username già registrato"));
        }
        Utente utente = new Utente();
        utente.setUsername(request.username());
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));

        return ResponseEntity.ok(utenteRepository.save(utente));
    }

}
