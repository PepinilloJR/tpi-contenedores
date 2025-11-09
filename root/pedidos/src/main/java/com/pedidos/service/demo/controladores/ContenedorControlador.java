package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.servicios.ContenedorServicio;

import com.pedidos.service.demo.entidades.Contenedor;
import com.commonlib.ContenedorDto;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorControlador {
    private final ContenedorServicio servicio;

    public ContenedorControlador(ContenedorServicio servicio) {
        this.servicio = servicio;
    }

        // helper privado entidad -> dto
    private ContenedorDto convertirDto(Contenedor c) {
        if (c == null)
            return null;
        return new ContenedorDto(c.getId(), c.getPeso(), c.getVolumen(), c.getEstado(), c.getCostoVolumen());
    }

    // helper privado dto -> entidad (para crear)
    private Contenedor convertirEntidad(ContenedorDto dto) {
        Contenedor c = new Contenedor();
        c.setNombre(dto.nombre());
        c.setApellido(dto.apellido());
        c.setTelefono(dto.telefono());
        c.setDireccion(dto.direccion());
        c.setDni(dto.dni());
        return c;
    }




}
