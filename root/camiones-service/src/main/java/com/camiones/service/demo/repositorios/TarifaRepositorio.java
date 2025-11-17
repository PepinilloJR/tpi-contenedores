package com.camiones.service.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commonlib.entidades.Tarifa;

public interface TarifaRepositorio extends JpaRepository<Tarifa, Long> {

}
