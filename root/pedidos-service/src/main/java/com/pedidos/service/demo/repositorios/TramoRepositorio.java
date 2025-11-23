package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Tramo;

import java.util.List;

public interface TramoRepositorio extends JpaRepository<Tramo, Long>{
    //query con del tramo con un determinado id de ruta
    List<Tramo> findByRutaId(Long id);

}
