package com.tpi.depositosservice.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonlib.entidades.Deposito;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    
  
    List<Deposito> findByNombre(String nombre);
}