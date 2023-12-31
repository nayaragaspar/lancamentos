package com.nayaragaspar.lancamentos.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.lancamentos.model.dto.SalvarClienteDto;
import com.nayaragaspar.lancamentos.model.entity.Cliente;
import com.nayaragaspar.lancamentos.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("cliente")
@RequiredArgsConstructor
@Tag(name = "Cliente")
public class ClienteController {
    private final ClienteService clienteService;

    @Operation(summary = "Buscar todos os clientes")
    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @Operation(summary = "Cadastrar cliente")
    @PostMapping
    public ResponseEntity<Cliente> save(@Valid @RequestBody SalvarClienteDto cliente) {
        return ResponseEntity.ok(clienteService.save(cliente));
    }
}
