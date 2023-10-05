package com.nayaragaspar.lancamentos.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayaragaspar.lancamentos.model.entity.Conta;
import com.nayaragaspar.lancamentos.model.entity.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByContaOrderByDataTransacaoDesc(Conta conta);
}
