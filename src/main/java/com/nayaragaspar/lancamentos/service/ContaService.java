package com.nayaragaspar.lancamentos.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nayaragaspar.lancamentos.exception.NotFoundException;
import com.nayaragaspar.lancamentos.model.dto.SalvarContaDto;
import com.nayaragaspar.lancamentos.model.dto.TransacaoDto;
import com.nayaragaspar.lancamentos.model.entity.Cliente;
import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.entity.Transacao;
import com.nayaragaspar.lancamentos.repository.ContaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final ClienteService clienteService;
    private final TransacaoService transacaoService;

    public List<Conta> findAll() {
        return contaRepository.findAll();
    }

    public BigDecimal getSaldo(UUID id) {
        return contaRepository.findSaldoById(id).orElseThrow(() -> new NotFoundException("Conta não encontrado"));
    }

    public Conta findById(UUID id) {
        return contaRepository.findById(id).orElseThrow(() -> new NotFoundException("Conta não encontrado"));
    }

    public List<Transacao> findTransactionsByConta(UUID idConta) {
        try {
            Conta conta = findById(idConta);

            return transacaoService.findByConta(conta);
        } catch (NotFoundException e) {
            throw e;
        }
    }

    public Conta save(SalvarContaDto conta) {
        try {
            Cliente cliente = clienteService.findById(conta.clienteId());

            return contaRepository.save(conta.toModel(cliente));
        } catch (NotFoundException e) {
            throw e;
        }
    }

    @Transactional
    public void transaction(UUID idConta, List<TransacaoDto> transacoes) {
        Conta conta = contaRepository.findByIdWithLock(idConta)
                .orElseThrow(() -> new NotFoundException("Conta não encontrado"));

        List<Transacao> transacoesModel = new ArrayList<>();
        BigDecimal[] saldoConta = { conta.getSaldo() };

        transacoes.stream().forEach((transacao) -> {
            transacoesModel.add(transacao.toModel(conta));
            saldoConta[0] = transacaoService.getSaldo(saldoConta[0], transacao.valor(), transacao.tipoTransacao());
        });

        transacaoService.saveAll(transacoesModel);
        contaRepository.updateSaldo(idConta, saldoConta[0]);
    }

}
