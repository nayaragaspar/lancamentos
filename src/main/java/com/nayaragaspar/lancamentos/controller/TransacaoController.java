package com.nayaragaspar.lancamentos.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.lancamentos.model.entity.Transacao;
import com.nayaragaspar.lancamentos.service.TransacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("transacao")
@RequiredArgsConstructor
@Tag(name = "Transações")
public class TransacaoController {
    private final TransacaoService transacaoService;

    @Operation(summary = "Buscar transação por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Transacao> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(transacaoService.findById(id));
    }

}
