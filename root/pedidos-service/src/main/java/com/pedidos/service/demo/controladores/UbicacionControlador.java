package com.pedidos.service.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.entidades.Ubicacion;
import com.commonlib.error.ErrorRequest;
import com.pedidos.service.demo.dto.UbicacionDtoIn;
import com.pedidos.service.demo.dto.UbicacionDtoOut;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.servicios.UbicacionServicio;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionControlador {
    private final UbicacionServicio servicio;

    public UbicacionControlador(UbicacionServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una Ubicacion", description = "Crea una Ubicacion")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody UbicacionDtoIn ubicacionDto) {
        Ubicacion ubicacionCreada;
        try {
            ubicacionCreada = servicio.crear(ubicacionDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }

        UbicacionDtoOut ubicacionDtoOut = new UbicacionDtoOut(ubicacionCreada.getId(),
                ubicacionCreada.getLatitud(), ubicacionCreada.getLongitud(), ubicacionCreada.getTipo().toString(),
                ubicacionCreada.getNombre(), ubicacionCreada.getCostoEstadia());
        return ResponseEntity.status(201).body(ubicacionDtoOut);
    }

    @Operation(summary = "Actualizar una Ubicacion", description = "Actualiza una Ubicacion dada por id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody UbicacionDtoIn ubicacionDto) {
        Ubicacion ubicacionActualizada;
        try {
            ubicacionActualizada = servicio.actualizar(id, ubicacionDto);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorRequest(400, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }
        UbicacionDtoOut ubicacionDtoOut = new UbicacionDtoOut(
                ubicacionActualizada.getId(),
                ubicacionActualizada.getLatitud(),
                ubicacionActualizada.getLongitud(),
                ubicacionActualizada.getTipo().toString(),
                ubicacionActualizada.getNombre(),
                ubicacionActualizada.getCostoEstadia());

        return ResponseEntity.ok(ubicacionDtoOut);
    }

    @Operation(summary = "Obtener una Ubicacion", description = "Obtiene una Ubicacion dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Ubicacion ubicacion;
        try {
            ubicacion = servicio.obtenerPorId(id);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }

        UbicacionDtoOut ubicacionDtoOut = new UbicacionDtoOut(
                ubicacion.getId(),
                ubicacion.getLatitud(),
                ubicacion.getLongitud(),
                ubicacion.getTipo().toString(),
                ubicacion.getNombre(),
                ubicacion.getCostoEstadia());

        return ResponseEntity.ok(ubicacionDtoOut);
    }

    @Operation(summary = "Obtener todas las Ubicaciones", description = "Obteniene todas las Ubicaciones")
    @GetMapping
    public ResponseEntity<List<UbicacionDtoOut>> obtenerTodos() {
        List<Ubicacion> lista = servicio.listarTodos();
        List<UbicacionDtoOut> dtos = lista.stream().map((u) -> {
            return new UbicacionDtoOut(
                u.getId(),
                u.getLatitud(),
                u.getLongitud(),
                u.getTipo().toString(),
                u.getNombre(),
                u.getCostoEstadia());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar una Ubicacion", description = "Elimina una Ubicacion dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            servicio.eliminar(id);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }
        return ResponseEntity.noContent().build();
    }

}
