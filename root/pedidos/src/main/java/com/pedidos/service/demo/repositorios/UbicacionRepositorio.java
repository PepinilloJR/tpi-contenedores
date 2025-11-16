package com.pedidos.service.demo.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Ubicacion;

public interface UbicacionRepositorio extends JpaRepository<Ubicacion, Long> {
    Optional<Ubicacion> findByLatitudEqualsAndLongitudEquals(Double latitud, Double longitud);
}
