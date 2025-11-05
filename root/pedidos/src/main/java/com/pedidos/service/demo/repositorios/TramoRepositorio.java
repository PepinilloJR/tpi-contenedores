package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedidos.service.demo.entidades.Tramo;

public interface TramoRepositorio extends JpaRepository<Tramo, Long>{
    
}
