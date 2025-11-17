package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.dto.UbicacionDto;
import com.commonlib.entidades.Ubicacion;
import com.pedidos.service.demo.servicios.UbicacionServicio;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.commonlib.dto.DtoHandler;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionControlador {
    private final UbicacionServicio servicio;

    public UbicacionControlador(UbicacionServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una Ubicacion", description = "Crea una Ubicacion")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody UbicacionDto ubicacionDto) {
        Ubicacion ubicacionEntidad = DtoHandler.convertirUbicacionEntidad(ubicacionDto);
        if (ubicacionDto.costo() == null && ubicacionDto.tipo() == "deposito") {
            return ResponseEntity.badRequest().body("Error al crear, un deposito requiere un costo de estadia");
        }
        Ubicacion ubicacionCreada = servicio.crear(ubicacionEntidad);

        return ResponseEntity.status(201).body(DtoHandler.convertirUbicacionDto(ubicacionCreada));
    }

    @Operation(summary = "Actualizar una Ubicacion", description = "Actualiza una Ubicacion dada por id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody UbicacionDto ubicacionDto) {

        Ubicacion ubicacionActual = servicio.obtenerPorId(id);

        ubicacionActual.setNombre(ubicacionDto.nombre() != null ? ubicacionDto.nombre() : ubicacionActual.getNombre());
        // ubicacionActual.setLatitud(ubicacionDto.latitud() != null ? ubicacionDto.latitud() : ubicacionActual.getLatitud());
        // ubicacionActual.setLongitud(ubicacionDto.longitud() != null ? ubicacionDto.longitud() : ubicacionActual.getLongitud());
        ubicacionActual.setCosto(ubicacionDto.costo() != null ? ubicacionDto.costo() : ubicacionActual.getCosto());
        Ubicacion ubicacionActualizada = servicio.actualizar(id, ubicacionActual);

        if (ubicacionDto.costo() == null && ubicacionDto.tipo() == "deposito") {
            return ResponseEntity.badRequest().body("Error al actualizar, un deposito requiere un costo de estadia");
        }

        return ResponseEntity.ok(DtoHandler.convertirUbicacionDto(ubicacionActualizada));
    }

    @Operation(summary = "Obtener una Ubicacion", description = "Obtiene una Ubicacion dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDto> obtener(@PathVariable Long id) {
        Ubicacion ubicacion = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirUbicacionDto(ubicacion));
    }

    @Operation(summary = "Obtener todas las Ubicaciones", description = "Obteniene todas las Ubicaciones")
    @GetMapping
    public ResponseEntity<List<UbicacionDto>> obtenerTodos() {
        List<Ubicacion> lista = servicio.listarTodos();
        List<UbicacionDto> dtos = lista.stream().map(DtoHandler::convertirUbicacionDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar una Ubicacion", description = "Elimina una Ubicacion dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
