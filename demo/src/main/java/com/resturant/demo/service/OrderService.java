package com.resturant.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.resturant.demo.dto.OrderRequest;
import com.resturant.demo.repository.OrderRepository;
import com.resturant.demo.repository.ProductRepository;
import com.resturant.demo.repository.TavoloRepository;
import com.resturant.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.resturant.demo.model.Order;
import com.resturant.demo.model.Tavolo;
import com.resturant.demo.model.User;
import com.resturant.demo.model.Prodotto;
import com.resturant.demo.model.OrderItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final TavoloRepository tavoloRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        Tavolo table = tavoloRepository.findById(orderRequest.tableId())
                .orElseThrow(() -> new RuntimeException("Tavolo non trovato"));
        User cameriere = userRepository.findById(orderRequest.cameriereId())
                .orElseThrow(() -> new RuntimeException("Cameriere non trovato"));

        // Creazione dell'oggetto ordine
        Order order = new Order();
        order.setTavolo(table);
        order.setCameriere(cameriere);
        order.setStatus("ATTESA");
        order.setDate(LocalDateTime.now());

        double runningTotale = 0;
        List<OrderItem> details = new ArrayList<>();

        // Ciclo sulla lista dei prodotti selezionati
        for (var itemRequest : orderRequest.items()) {
            Prodotto prodotto = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
            OrderItem detail = new OrderItem();
            detail.setOrder(order);
            detail.setProdotto(prodotto);
            detail.setQuantity(itemRequest.qta());
            detail.setNotes(itemRequest.note());

            detail.setPriceAtOrder(prodotto.getPrezzo());

            runningTotale += prodotto.getPrezzo() * itemRequest.qta();

            details.add(detail);
        }

        order.setItems(details);
        order.setTotalPrice(runningTotale);

        return orderRepository.save(order);
    }

    @Transactional
    public Order modifyOrder(Long id, OrderRequest orderRequest) {
        // Cerco l'ordine esistente
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        if ("CONSEGNATO".equals(order.getStatus()))
            throw new RuntimeException("Non puoi modificare un ordine già consegnato");

        Tavolo tavolo = tavoloRepository.findById(orderRequest.tableId())
                .orElseThrow(() -> new RuntimeException("Tavolo non trovato"));

        order.setTavolo(tavolo);

        order.getItems().clear();

        double newTotal = 0;

        for (var itemReq : orderRequest.items()) {
            Prodotto prodotto = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            OrderItem detail = new OrderItem();
            detail.setOrder(order);
            detail.setProdotto(prodotto);
            detail.setQuantity(itemReq.qta());
            detail.setNotes(itemReq.note());
            detail.setPriceAtOrder(prodotto.getPrezzo());

            newTotal += prodotto.getPrezzo() * itemReq.qta();
            order.getItems().add(detail);
        }

        order.setTotalPrice(newTotal);
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        orderRepository.delete(order);
    }
}
