package com.pedidos.service.demo.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long>{
    List<Cliente> findByDni(Integer dni);
    List<Cliente> findByNombre(String nombre);
}
