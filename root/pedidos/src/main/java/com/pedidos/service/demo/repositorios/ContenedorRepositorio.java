package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidos.service.demo.entidades.Contenedor;

public interface ContenedorRepositorio extends JpaRepository<Contenedor, Long>{
    
}
