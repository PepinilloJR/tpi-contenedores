package com.pedidos.service.demo.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.Enums.EstadosContenedor;
import com.commonlib.entidades.Contenedor;

public interface ContenedorRepositorio extends JpaRepository<Contenedor, Long> {
    // Buscar por disponibilidad
    Optional<Contenedor> findByEstadoOrderByIdAsc(EstadosContenedor estado);
    List<Contenedor> findByEstado(EstadosContenedor estado);
}
