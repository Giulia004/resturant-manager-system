package com.resturant.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	CommandLineRunner run(UserRepository userRepository,PizzaRepository pizzaRepository,TavoloRepository tavoloRepository,OrderRepository orderRepository) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword("admin");
				admin.setRole(Role.ADMIN);
				userRepository.save(admin);
			} else {
				System.out.println("Admin user already exists");
			}
			
			//Create pizzas
			if (pizzaRepository.count() == 0) {
				Pizza margherita = new Pizza(null,"Margherita", List.of("Tomato", "Mozzarella", "Basil"), 5.0);
				Pizza pepperoni = new Pizza(null,"Pepperoni", List.of("Tomato", "Mozzarella", "Pepperoni"), 6.5);
				Pizza veggie = new Pizza(null,"Veggie", List.of("Tomato", "Mozzarella", "Bell Peppers", "Onions", "Olives"), 7.0);
				pizzaRepository.saveAll(List.of(margherita, pepperoni, veggie));
				System.out.println("Sample pizzas created");
			} else {
				System.out.println("Pizzas already exist");
			}
			
			if(tavoloRepository.count() == 0) {
				tavoloRepository.saveAll(List.of(
					new Tavolo(null,1,4),
					new Tavolo(null,2,2),
					new Tavolo(null,3,6)
				));
				System.out.println("Sample tables created");
			} else {
				System.out.println("Tables already exist");
			}

			if(orderRepository.count() == 0) {
                User cameriere = userRepository.findByUsername("admin").get(); // esempio: admin come cameriere
                Tavolo tavolo1 = tavoloRepository.findById(1L).get();
                List<Pizza> ordinePizze = pizzaRepository.findAll().subList(0, 2); // esempio: prime 2 pizze

				Order ordine1 = new Order();
                ordine1.setCameriere(cameriere);
                ordine1.setTavolo(tavolo1);
                ordine1.setStatus("In preparazione");
                ordine1.setPizzas(ordinePizze);
                ordine1.setTotalPrice(ordinePizze.stream().mapToDouble(Pizza::getPrice).sum());

                orderRepository.save(ordine1);

                System.out.println("Ordine di esempio creato");
            } else {
                System.out.println("Ordini già presenti");
            }
		};
	}
}
