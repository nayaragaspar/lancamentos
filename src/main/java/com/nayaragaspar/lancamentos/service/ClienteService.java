package com.nayaragaspar.lancamentos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nayaragaspar.lancamentos.exception.CustomBadRequestException;
import com.nayaragaspar.lancamentos.exception.NotFoundException;
import com.nayaragaspar.lancamentos.model.dto.SalvarClienteDto;
import com.nayaragaspar.lancamentos.model.entity.Cliente;
import com.nayaragaspar.lancamentos.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(UUID id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    public Cliente save(SalvarClienteDto cliente) {
        try {
            return clienteRepository.save(cliente.toModel());
        } catch (DataIntegrityViolationException e) {
            throw new CustomBadRequestException("Cliente já cadastrado!", cliente);
        }

    }
}
