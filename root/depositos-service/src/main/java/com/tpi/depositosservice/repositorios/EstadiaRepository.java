package com.tpi.depositosservice.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonlib.entidades.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {
    boolean existsByIdContenedorAndFechaFinIsNull(Long idContenedor);
}