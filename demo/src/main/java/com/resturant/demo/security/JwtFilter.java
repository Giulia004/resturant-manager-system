package com.resturant.demo.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.resturant.demo.service.CustomUserDetailsService;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setStatus(HttpServletResponse.SC_OK);
            return; // Fermati qui, non andare avanti con il controllo JWT!
        }

        // 1. Verifica se l'header è presente e corretto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Estrai il token e lo username
        jwt = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);

        // 3. Se abbiamo uno username e l'utente non è già autenticato nel contesto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. Valida il token rispetto ai dettagli dell'utente
            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Aggiorna il Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continua la catena dei filtri
        filterChain.doFilter(request, response);
    }
}