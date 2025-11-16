package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.dto.RutaDto;
import com.commonlib.entidades.Ruta;
import com.pedidos.service.demo.servicios.RutaServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.commonlib.dto.DtoHandler;


// Hacer una interfaz de uso comun para el manejo de los dto

@RestController
@RequestMapping("/api/rutas")
public class RutaControlador {
    private final RutaServicio servicio;

    public RutaControlador(RutaServicio servicio) {
        this.servicio = servicio;
    }


    @PostMapping
    public ResponseEntity<RutaDto> crear(@RequestBody RutaDto rutaDto) {
        System.out.println("===========================");
        System.out.println(rutaDto);
        Ruta rutaEntidad = DtoHandler.convertirRutaEntidad(rutaDto);
        System.out.println("===========================");
        System.out.println("===========================");
        System.out.println(rutaEntidad.toString());
        Ruta rutaCreada = servicio.crear(rutaEntidad);
        return ResponseEntity.status(201).body(DtoHandler.convertirRutaDto(rutaCreada));
    }

    // Recordar siempre lo de actualizacion parcial

    @PutMapping("/{id}")
    public ResponseEntity<RutaDto> actualizar(@PathVariable Long id, @RequestBody RutaDto rutaDto) {

        Ruta rutaActual = servicio.obtenerPorId(id);

        rutaActual.setCantidadDepositos(rutaDto.cantidadDepositos() != null ? rutaDto.cantidadDepositos() : rutaActual.getCantidadDepositos());
        rutaActual.setCantidadTramos(rutaDto.cantidadTramos() != null ? rutaDto.cantidadTramos() : rutaActual.getCantidadTramos());
        rutaActual.setCostoPorTramo(rutaDto.costoPorTramo() != null ? rutaDto.costoPorTramo() : rutaActual.getCostoPorTramo());

        Ruta rutaActualizada = servicio.actualizar(id, rutaActual);

        return ResponseEntity.ok(DtoHandler.convertirRutaDto(rutaActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutaDto> obtener(@PathVariable Long id) {
        Ruta ruta = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirRutaDto(ruta));
    }

    @GetMapping
    public ResponseEntity<List<RutaDto>> obtenerTodos() {
        List<Ruta> lista = servicio.listarTodos();
        List<RutaDto> dtos = lista.stream().map(DtoHandler::convertirRutaDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
