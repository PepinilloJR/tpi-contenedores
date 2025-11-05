package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidos.service.demo.entidades.Ruta;

public interface RutaRepositorio extends JpaRepository<Ruta, Long>{
    
}
