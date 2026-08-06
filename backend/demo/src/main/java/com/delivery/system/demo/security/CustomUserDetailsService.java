package com.delivery.system.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.delivery.system.demo.repository.UtenteRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    public final UtenteRepository repository;

    public CustomUserDetailsService(UtenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }
}
