package com.nayaragaspar.lancamentos.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.lancamentos.model.dto.SalvarContaDto;
import com.nayaragaspar.lancamentos.model.dto.TransacaoDto;
import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.entity.Transacao;
import com.nayaragaspar.lancamentos.service.ContaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService contaService;

    @GetMapping
    public ResponseEntity<List<Conta>> findAll() {
        return ResponseEntity.ok(contaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(contaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Conta> save(@Valid @RequestBody SalvarContaDto conta) {
        return ResponseEntity.ok(contaService.save(conta));
    }

    @PostMapping("/{id}/transacao")
    public ResponseEntity<String> transaction(@PathVariable UUID id,
            @RequestBody List<@Valid TransacaoDto> transacoes) {
        if (transacoes.size() > 0)
            contaService.transaction(id, transacoes);
        else
            return new ResponseEntity<>("Nenhuma transação encontrada.", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok("Transações realizadas com sucesso!");
    }

    @GetMapping("/{id}/transacao")
    public ResponseEntity<List<Transacao>> getTransactionsByConta(@PathVariable UUID id) {
        return ResponseEntity.ok(contaService.findTransactionsByConta(id));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> getSaldo(@PathVariable UUID id) {
        return ResponseEntity.ok(contaService.getSaldo(id));
    }
}
