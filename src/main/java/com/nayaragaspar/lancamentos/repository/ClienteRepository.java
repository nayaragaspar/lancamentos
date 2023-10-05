package com.nayaragaspar.lancamentos.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayaragaspar.lancamentos.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

}
