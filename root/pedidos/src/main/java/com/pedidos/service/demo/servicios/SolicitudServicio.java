package com.pedidos.service.demo.servicios;


import org.springframework.stereotype.Service;

import com.pedidos.service.demo.entidades.Solicitud;
import com.pedidos.service.demo.repositorios.SolicitudRepositorio;

import lombok.RequiredArgsConstructor;

// Calcular costo y el tiempo estimado inicial
// crear la solicitud y asociar al contenedor y al cliente
// setear el estado inicial en pendiente

// ruta tentativa?

@Service
@RequiredArgsConstructor
public class SolicitudServicio {
    private final SolicitudRepositorio repositorio;


    public Iterable<Solicitud> getAll() {
        return repositorio.findAll();
    }

}
