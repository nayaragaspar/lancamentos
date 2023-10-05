package com.nayaragaspar.lancamentos.model.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.nayaragaspar.lancamentos.model.enums.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Transacao> transacoes;
    @Column
    private BigDecimal saldo;
    @Column
    private boolean ativo;
}
