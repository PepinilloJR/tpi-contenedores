package com.tpi.depositosservice.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonlib.entidades.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByNombre(String nombre);

    Optional<Ubicacion> findById(Long id);

    Optional<Ubicacion> findByLatitudAndLongitud(Double latitud, Double longitud);
}