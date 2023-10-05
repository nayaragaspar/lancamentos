package com.nayaragaspar.lancamentos.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.entity.Transacao;
import com.nayaragaspar.lancamentos.model.enums.TipoTransacao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransacaoDto(@NotNull TipoTransacao tipoTransacao, @NotNull @Min(0) BigDecimal valor) {
    public Transacao toModel(Conta conta) {
        return new Transacao(null, conta, valor, tipoTransacao, new Date());
    }
}
