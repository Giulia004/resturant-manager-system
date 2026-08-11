package com.delivery.system.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.system.dto.request.PagamentoRequest;
import com.service.PagamentoService;

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
