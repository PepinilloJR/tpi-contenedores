package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Solicitud;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepositorio extends JpaRepository<Solicitud, Long>{
    List<Solicitud> findByClienteId(Long id);
    Optional<Solicitud> findByIdAndClienteId(Long id, Long clienteId);
    Optional<Solicitud> findByContenedorId(Long contenedorId);
}
