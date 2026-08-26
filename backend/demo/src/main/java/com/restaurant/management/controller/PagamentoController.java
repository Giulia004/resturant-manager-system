package com.restaurant.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.management.service.PagamentoService;
import com.restaurant.management.dto.request.PagamentoRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ordini")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CASSIERE','ADMIN')")
public class PagamentoController {
    private final PagamentoService pagamentoService;

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<Void> creaPagamento(@PathVariable("id") Long ordineId, @RequestBody PagamentoRequest request) {
        pagamentoService.processaPagamento(ordineId, request);
        return ResponseEntity.ok().build();
    }
}
