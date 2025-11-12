package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.RutaDto;
import com.pedidos.service.demo.entidades.Ruta;
import com.pedidos.service.demo.servicios.RutaServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


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
        return ResponseEntity.status(201).body(convertirRutaDto(rutaCreada));
    }

    // Recordar siempre lo de actualizacion parcial

    @PutMapping("/{id}")
    public ResponseEntity<RutaDto> actualizar(@PathVariable Long id, @RequestBody RutaDto rutaDto) {

        Ruta rutaActual = servicio.obtenerPorId(id);

        rutaActual.setCantidadDepositos(rutaDto.cantidadDepositos() != null ? rutaDto.cantidadDepositos() : rutaActual.getCantidadDepositos());
        rutaActual.setCantidadTramos(rutaDto.cantidadTramos() != null ? rutaDto.cantidadTramos() : rutaActual.getCantidadTramos());
        rutaActual.setCostoPorTramo(rutaDto.costoPorTramo() != null ? rutaDto.costoPorTramo() : rutaActual.getCostoPorTramo());

        Ruta rutaActualizada = servicio.actualizar(id, rutaActual);

        return ResponseEntity.ok(convertirRutaDto(rutaActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutaDto> obtener(@PathVariable Long id) {
        Ruta ruta = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirRutaDto(ruta));
    }

    @GetMapping
    public ResponseEntity<List<RutaDto>> obtenerTodos() {
        List<Ruta> lista = servicio.listarTodos();
        List<RutaDto> dtos = lista.stream().map(this::convertirRutaDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
