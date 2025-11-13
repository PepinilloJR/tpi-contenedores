package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.servicios.SolicitudServicio;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudControlador {
    private final SolicitudServicio servicio;

    public SolicitudControlador(SolicitudServicio servicio) {
        this.servicio = servicio;
    }

    
}
