package com.resturant.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.resturant.demo.repository.*;
import com.resturant.demo.model.*;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

        public static void main(String[] args) {
                SpringApplication.run(DemoApplication.class, args);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        CommandLineRunner run(UserRepository userRepository,
                        ProductRepository productRepository,
                        TavoloRepository tavoloRepository,
                        PasswordEncoder passwordEncoder) {
                return args -> {

                        // 1. GESTIONE UTENTI (Admin e Cameriere)
                        creaOAggiornaUser(userRepository, passwordEncoder, "admin", Role.ADMIN);
                        creaOAggiornaUser(userRepository, passwordEncoder, "cameriere1", Role.CAMERIERE);
                        // Utente specifico per il tablet fisso del Tavolo 1
                        creaOAggiornaUser(userRepository, passwordEncoder, "tavolo1", Role.USER);

                        if (productRepository.count() == 0) {
                                productRepository.saveAll(List.of(
                                                // PIZZE
                                                new Prodotto(null, "Margherita", "Pomodoro, Mozzarella, Basilico", 6.5,
                                                                true, Categoria.PIZZA),
                                                new Prodotto(null, "Diavola", "Pomodoro, Mozzarella, Salamino piccante",
                                                                8.0, true,
                                                                Categoria.PIZZA),
                                                new Prodotto(null, "Quattro Formaggi",
                                                                "Mozzarella, Gorgonzola, Fontina, Parmigiano", 9.5,
                                                                true,
                                                                Categoria.PIZZA),

                                                // PRIMI
                                                new Prodotto(null, "Spaghetti alla Carbonara",
                                                                "Guanciale, Uovo, Pecorino", 12.0, true,
                                                                Categoria.PRIMI),
                                                new Prodotto(null, "Lasagna alla Bolognese",
                                                                "Ragù di carne e besciamella", 11.0, true,
                                                                Categoria.PRIMI),

                                                // SECONDI
                                                new Prodotto(null, "Tagliata di Manzo", "Rucola e scaglie di Grana",
                                                                18.0, true,
                                                                Categoria.SECONDI),
                                                new Prodotto(null, "Cotoletta alla Milanese", "Con patatine fritte",
                                                                14.0, true,
                                                                Categoria.SECONDI),

                                                // BEVANDE
                                                new Prodotto(null, "Acqua Minerale 1L", "Naturale o Frizzante", 2.5,
                                                                true, Categoria.BEVANDA),
                                                new Prodotto(null, "Birra Media 0.4L", "Bionda alla spina", 5.0, true,
                                                                Categoria.BEVANDA),
                                                new Prodotto(null, "Vino della Casa 0.5L", "Rosso o Bianco", 6.0, true,
                                                                Categoria.BEVANDA),

                                                // DESSERT
                                                new Prodotto(null, "Tiramisù Artigianale", "Al caffè e mascarpone", 5.5,
                                                                true,
                                                                Categoria.DESSERT),
                                                new Prodotto(null, "Panna Cotta", "Ai frutti di bosco", 5.0, true,
                                                                Categoria.DESSERT)));
                        }

                        // 3. CREAZIONE TAVOLI
                        if (tavoloRepository.count() == 0) {
                                User user1 = userRepository.findByUsername("tavolo1").orElseThrow();
                                tavoloRepository.saveAll(List.of(
                                                new Tavolo(null, 1, 4, "LIBERO", user1.getId()),
                                                new Tavolo(null, 2, 2, "LIBERO", null),
                                                new Tavolo(null, 3, 6, "LIBERO", null),
                                                new Tavolo(null, 4, 4, "LIBERO", null),
                                                new Tavolo(null, 5, 10, "LIBERO", null)));
                        }
                };
        }

        private void creaOAggiornaUser(UserRepository repo, PasswordEncoder encoder, String username, Role role) {
                User user = repo.findByUsername(username).orElse(new User());
                user.setUsername(username);
                user.setPassword(encoder.encode("password123")); // Password di default per i test
                user.setRole(role);
                repo.save(user);
                System.out.println(">> Utente pronto: " + username + " (Password: password123)");
        }
}