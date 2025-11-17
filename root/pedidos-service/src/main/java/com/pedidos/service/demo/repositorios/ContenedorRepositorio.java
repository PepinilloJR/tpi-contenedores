package com.pedidos.service.demo.repositorios;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Contenedor;

public interface ContenedorRepositorio extends JpaRepository<Contenedor, Long> {
    // Buscar por disponibilidad
    Optional<Contenedor> findByEstadoOrderByIdAsc(String estado);
    List<Contenedor> findByEstado(String estado);
}
