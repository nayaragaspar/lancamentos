package com.nayaragaspar.lancamentos.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.nayaragaspar.lancamentos.model.entity.Cliente;
import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.enums.TipoConta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SalvarContaDto(@NotNull UUID clienteId, @NotNull TipoConta tipoConta, @Min(0) BigDecimal saldo) {
    public Conta toModel(Cliente cliente) {
        return new Conta(null, cliente, tipoConta, null, saldo, true);
    }
}
