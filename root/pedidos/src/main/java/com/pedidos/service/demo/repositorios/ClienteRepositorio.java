package com.pedidos.service.demo.repositorios;

import com.pedidos.service.demo.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long>{
    List<Cliente> findByNombre(String nombre);
}
