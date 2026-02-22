package com.resturant.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.resturant.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import com.resturant.demo.model.Prodotto;
import com.resturant.demo.model.Categoria;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProdottoController {
    private final ProductRepository productRepository;

    @GetMapping
    public List<Prodotto> getAllDisponibili() {
        return productRepository.findByDisponibileTrue();
    }

    @GetMapping("/categoria/{cat}")
    public List<Prodotto> findByCategory(@PathVariable Categoria cat) {
        return productRepository.findByCategoriaAndDisponibileTrue(cat);
    }

    @GetMapping("/search")
    public List<Prodotto> search(@RequestParam String name) {
        return productRepository.findByNomeContaining(name);
    }

    // --- Metodi di Gestione (CRUD) ---

    @PostMapping
    public Prodotto createProdotto(@RequestBody Prodotto prodotto) {
        return productRepository.save(prodotto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProdotto(@PathVariable Long id, @RequestBody Prodotto prodottoDetails) {
        return productRepository.findById(id).map(prodotto -> {
            prodotto.setNome(prodottoDetails.getNome());
            prodotto.setDescrizione(prodottoDetails.getDescrizione());
            prodotto.setPrezzo(prodottoDetails.getPrezzo());
            prodotto.setDisponibile(prodottoDetails.isDisponibile());
            prodotto.setCategoria(prodottoDetails.getCategoria());
            return ResponseEntity.ok(productRepository.save(prodotto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
