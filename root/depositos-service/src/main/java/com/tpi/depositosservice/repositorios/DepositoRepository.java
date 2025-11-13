package com.tpi.depositosservice.repositorios;

import com.tpi.depositosservice.entidades.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    
  
    List<Deposito> findByNombre(String nombre);
}