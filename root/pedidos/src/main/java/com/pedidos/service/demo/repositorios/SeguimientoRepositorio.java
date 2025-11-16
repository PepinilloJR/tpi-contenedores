package com.pedidos.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Seguimiento;

public interface SeguimientoRepositorio extends JpaRepository<Seguimiento, Long> {

}
