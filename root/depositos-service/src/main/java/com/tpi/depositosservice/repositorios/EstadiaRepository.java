package com.tpi.depositosservice.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonlib.entidades.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {
    List<Estadia> findByIdContenedor(Long idContenedor);
}