package com.camiones.service.demo.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Tarifa;

public interface TarifaRepositorio extends JpaRepository<Tarifa, Long> {

    // Buscar tarifas por nombre (puede haber varias con vigencias distintas)
    List<Tarifa> findByNombre(String nombre);

    // Buscar tarifas activas
    List<Tarifa> findByActivoTrue();

    // Buscar tarifas vigentes en una fecha específica (útil para Gateway)
    List<Tarifa> findByVigenciaDesdeLessThanEqualAndVigenciaHastaGreaterThanEqual(
            LocalDate desde,
            LocalDate hasta
    );

    // Buscar tarifas sin fecha de finalización (vigentes abiertas)
    List<Tarifa> findByVigenciaHastaIsNull();
}
