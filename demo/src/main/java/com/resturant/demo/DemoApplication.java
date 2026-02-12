package com.resturant.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.resturant.demo.repository.PizzaRepository;
import com.resturant.demo.repository.UserRepository;
import com.resturant.demo.repository.OrderRepository;
import com.resturant.demo.repository.TavoloRepository;
import com.resturant.demo.model.Pizza;
import com.resturant.demo.model.Role;
import com.resturant.demo.model.User;
import com.resturant.demo.model.Tavolo;
import com.resturant.demo.model.Order;

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
			PizzaRepository pizzaRepository,
			TavoloRepository tavoloRepository,
			OrderRepository orderRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {

			// 1. GESTIONE UTENTI (Reset per garantire BCrypt)
			// Invece di cancellare, cerchiamo e aggiorniamo la password
			creaOAggiornaUser(userRepository, passwordEncoder, "admin", Role.ADMIN);
			creaOAggiornaUser(userRepository, passwordEncoder, "admin2", Role.ADMIN);

			// 2. CREAZIONE PIZZE
			if (pizzaRepository.count() == 0) {
				pizzaRepository.saveAll(List.of(
						new Pizza(null, "Margherita", List.of("Pomodoro", "Mozzarella", "Basilico"), 5.0),
						new Pizza(null, "Pepperoni", List.of("Pomodoro", "Mozzarella", "Salamino"), 6.5),
						new Pizza(null, "Veggie", List.of("Pomodoro", "Mozzarella", "Verdure"), 7.0)));
				System.out.println(">> Pizze create correttamente");
			}

			// 3. CREAZIONE TAVOLI
			if (tavoloRepository.count() == 0) {
				tavoloRepository.saveAll(List.of(
						new Tavolo(null, 1, 4, "LIBERO"),
						new Tavolo(null, 2, 2, "OCCUPATO"),
						new Tavolo(null, 3, 6, "LIBERO")));
				System.out.println(">> Tavoli creati correttamente");
			}

			// 4. ORDINE DI ESEMPIO
			if (orderRepository.count() == 0) {
				// Recuperiamo l'utente creato sopra (sicuri che esista ora)
				User cameriere = userRepository.findByUsername("admin")
						.orElseThrow(() -> new RuntimeException("Admin non trovato per l'ordine"));

				// Recuperiamo il tavolo 1 (usiamo findAll per sicurezza se gli ID non partono
				// da 1)
				Tavolo tavolo1 = tavoloRepository.findAll().get(0);
				List<Pizza> primePizze = pizzaRepository.findAll().subList(0, 2);

				Order ordine1 = new Order();
				ordine1.setCameriere(cameriere);
				ordine1.setTavolo(tavolo1);
				ordine1.setStatus("In preparazione");
				ordine1.setPizzas(primePizze);
				ordine1.setTotalPrice(primePizze.stream().mapToDouble(Pizza::getPrice).sum());

				orderRepository.save(ordine1);
				System.out.println(">> Ordine di esempio creato correttamente");
			}
		};
	}

	// Metodo helper per non duplicare codice e non rompere i vincoli del DB
	private void creaOAggiornaUser(UserRepository repo, PasswordEncoder encoder, String username, Role role) {
		User user = repo.findByUsername(username).orElse(new User());
		user.setUsername(username);
		user.setPassword(encoder.encode("admin")); // Sovrascrive sempre con "admin" criptato
		user.setRole(role);
		repo.save(user);
		System.out.println(">> Utente " + username + " pronto (Password: admin)");
	}
}