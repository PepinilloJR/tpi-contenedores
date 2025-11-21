package com.pedidos.service.demo.repositorios;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombre(String nombre);

    Optional<Cliente> findByDni(Integer dni);

    boolean existsByDni(Integer dni);
}
