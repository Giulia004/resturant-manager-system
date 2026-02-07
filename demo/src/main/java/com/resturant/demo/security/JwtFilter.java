package com.resturant.demo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.ServletException;

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
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // 1️⃣ Controllo header
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

            // 2️⃣ Token valido?
            if (jwtUtil.validateToken(token))
                username = jwtUtil.extractUsername(token);
        }

        // 3️⃣ Se utente trovato e non già autenticato
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4️⃣ Carico utente dal DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5️⃣ Creo oggetto autenticazione
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6️⃣ Setto utente autenticato in Spring
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 7️⃣ Continua la catena filtri
        filterChain.doFilter(request, response);
    }

}
