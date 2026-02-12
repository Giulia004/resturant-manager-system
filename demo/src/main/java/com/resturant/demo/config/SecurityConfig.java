package com.resturant.demo.config;

import com.resturant.demo.security.AuthEntryPoint;
import com.resturant.demo.security.JwtFilter;
import com.resturant.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtFilter jwtFilter,
            AuthEntryPoint authEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pizze/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pizze/**").hasAnyAuthority("CUCINA", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/pizze/**").hasAnyAuthority("CUCINA", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/pizze/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/tavoli/**").hasAnyAuthority("CAMERIERE", "ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyAuthority("CAMERIERE", "CUCINA", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5175")); // Assicurati sia identico al browser
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Cache del preflight per un'ora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
