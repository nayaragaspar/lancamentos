package com.nayaragaspar.lancamentos.model.dto;

import com.nayaragaspar.lancamentos.model.entity.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SalvarClienteDto(@NotBlank String nome, @NotBlank String documento,
                @Email(message = "Informe um email válido!") String email,
                String telefone) {

        public Cliente toModel() {
                return new Cliente(null, nome, documento, email, telefone);
        }
}
