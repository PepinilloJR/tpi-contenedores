package com.tpi.depositosservice.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpi.depositosservice.entidades.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {

    List<Estadia> findByIdTramo(Long idTramo);
}