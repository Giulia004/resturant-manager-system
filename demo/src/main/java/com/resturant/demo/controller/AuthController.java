package com.resturant.demo.controller;

import com.resturant.demo.dto.AuthRequest;
import com.resturant.demo.dto.AuthResponse;
import com.resturant.demo.dto.RegisterRequest;
import com.resturant.demo.repository.UserRepository;
import com.resturant.demo.security.JwtUtil; // Assicurati che il percorso sia corretto
import com.resturant.demo.model.Role;
import com.resturant.demo.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Username già in uso", null));
        }
        
        //Create new User
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.psw()));
        newUser.setRole(Role.USER);
        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser.getUsername());

        return ResponseEntity.ok(new AuthResponse(token, newUser.getRole().name()));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("Tentativo di login: " + request.username());
        System.out.println("Password: "+request.psw());
        // 1. Tenta l'autenticazione
        try {//Autenticazione
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.psw()));
        } catch (Exception e) {
            //Eccezione lanciata nel caso le credenziali siano errate
            return ResponseEntity.status(401).body(new AuthResponse("Credenziali non valide", null));
        }

        //Recupero dell'utente dal DB
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 3. Generiamo il token
        String token = jwtUtils.generateToken(user.getUsername());

        // 4. Rispondiamo al frontend con il token e il ruolo (utile per React per
        // sapere cosa mostrare)
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }
}