package com.nayaragaspar.lancamentos.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nayaragaspar.lancamentos.model.entity.Conta;

import jakarta.persistence.LockModeType;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Conta c WHERE c.id = :id")
    Optional<Conta> findByIdWithLock(UUID id);

    @Modifying
    @Query("update Conta c set c.saldo = :saldo where c.id = :id")
    void updateSaldo(@Param(value = "id") UUID idConta, @Param(value = "saldo") BigDecimal saldo);

    @Query("SELECT c.saldo FROM Conta c WHERE c.id = :id")
    Optional<BigDecimal> findSaldoById(UUID id);
}
