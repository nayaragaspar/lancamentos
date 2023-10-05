package com.nayaragaspar.lancamentos.model.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.nayaragaspar.lancamentos.model.enums.TipoTransacao;

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
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @OneToMany(fetch = FetchType.LAZY)
    private Conta conta;
    @Column
    private BigDecimal valor;
    @Column
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;
    /* @ManyToOne
    private Conta contaOrigem; */
    @Column
    private Date dataTransacao;
}
