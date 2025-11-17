package com.tpi.depositosservice.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Tramo;

public interface TramoRepositorio extends JpaRepository<Tramo, Long>{
    //query con del tramo con un determinado id de ruta
    List<Tramo> findByRutaId(Long id);
    List<Tramo> findByCamionNombreTransportista(String nombreTransportista);
}
