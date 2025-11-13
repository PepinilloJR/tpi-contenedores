package com.tpi.depositosservice.repositorios;

import com.tpi.depositosservice.entidades.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {

    List<Estadia> findByIdTramo(Long idTramo);
}