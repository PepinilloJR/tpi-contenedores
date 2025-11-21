package com.camiones.service.demo.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Camion;

public interface CamionRepositorio extends JpaRepository<Camion, Long> {

    // Buscar por patente (única)
    List<Camion> findByPatente(String patente);

    // Verificar si ya existe una patente (para validar unicidad)
    boolean existsByPatente(String patente);

    // Buscar por transportista
    List<Camion> findByNombreTransportista(String nombreTransportista);

    // Buscar por disponibilidad
    Optional<Camion> findFirstByDisponibleTrueOrderByIdAsc();

    // Buscar disponibles y con capacidad determinada
    Optional<Camion> findFirstByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
            Double capacidadPeso, Double capacidadVolumen);
}
