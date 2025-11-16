package com.tpi.depositosservice.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.commonlib.entidades.Deposito;
import com.tpi.depositosservice.repositorios.DepositoRepository;

@Service
public class DepositoServicio {

    private final DepositoRepository repository;

    public DepositoServicio(DepositoRepository repository) {
        this.repository = repository;
    }

    public Deposito crear(Deposito deposito) {
        return repository.save(deposito);
    }

    public Deposito actualizar(Long id, Deposito deposito) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró el depósito con id: " + id);
        }
        deposito.setIdDeposito(id); // Aseguramos que el ID sea el correcto
        return repository.save(deposito);
    }

    public Deposito obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el depósito con id: " + id));
    }

    public List<Deposito> listarTodos() {
        return repository.findAll();
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró el depósito con id: " + id);
        }
        repository.deleteById(id);
    }
}