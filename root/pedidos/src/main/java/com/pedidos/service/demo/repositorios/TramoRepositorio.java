package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.pedidos.service.demo.entidades.Tramo;

public interface TramoRepositorio extends JpaRepository<Tramo, Long>{
    //query con del tramo con un determinado id de ruta
    List<Tramo> findByRutaId(Long id);
}
