package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.RutaDto;
import com.pedidos.service.demo.entidades.Ruta;
import com.pedidos.service.demo.servicios.RutaServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// Hacer una interfaz de uso comun para el manejo de los dto

@RestController
@RequestMapping("/api/clientes")
public class RutaControlador {
    private final RutaServicio servicio;

    public RutaControlador(RutaServicio servicio) {
        this.servicio = servicio;
    }

    private Ruta convertirRutaEntidad(RutaDto dto) {
        if (dto == null)
            return null;
        Ruta r = new Ruta();
        r.setId(dto.id());
        r.setCantidadTramos(dto.cantidadTramos());
        r.setCantidadDepositos(dto.cantidadDepositos());
        r.setCostoPorTramo(dto.costoPorTramo());
        return r;
    }

    private RutaDto convertirRutaDto(Ruta r) {
        if (r == null)
            return null;
        return new RutaDto(r.getId(), r.getCantidadTramos(), r.getCantidadDepositos(), r.getCostoPorTramo());
    }


    @PostMapping
    public ResponseEntity<RutaDto> crear(@RequestBody RutaDto rutaDto) {
        Ruta rutaEntidad = convertirRutaEntidad(rutaDto);
        Ruta rutaCreada = servicio.crear(rutaEntidad);
        
        return ResponseEntity.status(201).body(convertirRutaDto(null));
    }
    

}
