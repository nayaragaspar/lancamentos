package com.nayaragaspar.lancamentos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nayaragaspar.lancamentos.exception.CustomBadRequestException;
import com.nayaragaspar.lancamentos.exception.NotFoundException;
import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.entity.Transacao;
import com.nayaragaspar.lancamentos.model.enums.TipoTransacao;
import com.nayaragaspar.lancamentos.repository.TransacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;

    public Transacao findById(UUID id) {
        return transacaoRepository.findById(id).orElseThrow(() -> new NotFoundException("Transação não encontrado"));
    }

    public List<Transacao> findByConta(Conta conta) {
        return transacaoRepository.findByContaOrderByDataTransacaoDesc(conta);
    }

    public BigDecimal getSaldo(BigDecimal saldo, BigDecimal valor, TipoTransacao tipoTransacao) {

        switch (tipoTransacao) {
            case SAQUE:
                saldo = saldo.subtract(valor);
                break;
            case DEPOSITO:
                saldo = saldo.add(valor);
                break;
            default:
                throw new CustomBadRequestException("Operação inválida");
        }

        return saldo;
    }

    public List<Transacao> saveAll(List<Transacao> transacoes) {
        return transacaoRepository.saveAll(transacoes);
    }
}
