package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidos.service.demo.entidades.Solicitud;

public interface SolicitudRepositorio extends JpaRepository<Solicitud, Long>{
    
}
