package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.dto.UbicacionDto;
import com.commonlib.entidades.Ubicacion;
import com.commonlib.error.ErrorRequest;
import com.pedidos.service.demo.servicios.UbicacionServicio;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
 
import com.commonlib.dto.DtoHandler;
=======

import com.commonlib.Enums.TiposUbicacion;
import com.pedidos.service.demo.dto.UbicacionDtoIn;
import com.pedidos.service.demo.dto.UbicacionDtoOut;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
>>>>>>> e970425e5f74a7e345d5d8d11fa0ebb31a8b0829

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
            return  ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }
 
        UbicacionDtoOut ubicacionDtoOut = new UbicacionDtoOut(ubicacionCreada.getId(),
        ubicacionCreada.getLatitud(), ubicacionCreada.getLongitud(), ubicacionCreada.getTipo().toString(), ubicacionCreada.getNombre(), ubicacionCreada.getCostoEstadia());
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
            ubicacionActualizada.getCostoEstadia()
        );

        return ResponseEntity.ok(ubicacionDtoOut);
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
