package com.delivery.system.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.demo.model.Utente;
import com.delivery.system.demo.repository.UtenteRepository;
import com.delivery.system.demo.security.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authMnager;
    private final JwtUtil jwtUtil;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UtenteRepository utenteRepository,
            PasswordEncoder passwordEncoder) {
        this.authMnager = authManager;
        this.jwtUtil = jwtUtil;
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        authMnager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        Utente utente = utenteRepository.findByUsername(username).orElseThrow();
        String token = jwtUtil.generateToken(utente.getUsername(), utente.getRuolo().name());

        return ResponseEntity.ok(Map.of("token", token, "ruolo", utente.getRuolo()));
    }

    @PostMapping("/register")
    public ResponseEntity<Utente> register(@RequestBody Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        return ResponseEntity.ok(utenteRepository.save(utente));
    }
    
    
}
