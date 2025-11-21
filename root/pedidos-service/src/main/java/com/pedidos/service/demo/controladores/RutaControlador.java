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

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.RutaDto;
import com.commonlib.entidades.Ruta;
import com.commonlib.error.ErrorRequest;
import com.pedidos.service.demo.dto.RutaTentativaDtoIn;
import com.pedidos.service.demo.dto.RutaTentativaDtoOut;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.servicios.RutaServicio;

import io.swagger.v3.oas.annotations.Operation;

// Hacer una interfaz de uso comun para el manejo de los dto

@RestController
@RequestMapping("/api/rutas")
public class RutaControlador {
    private final RutaServicio servicio;

    public RutaControlador(RutaServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una Ruta", description = "Crea una Ruta")
    @PostMapping
    public ResponseEntity<RutaDto> crear(@RequestBody RutaDto rutaDto) {

        
        Ruta rutaEntidad = DtoHandler.convertirRutaEntidad(rutaDto);
        Ruta rutaCreada = servicio.crear(rutaEntidad);
        return ResponseEntity.status(201).body(DtoHandler.convertirRutaDto(rutaCreada));
    }

    // Recordar siempre lo de actualizacion parcial

    @Operation(summary = "Actualizar una Ruta", description = "Actualiza una Ruta")
    @PutMapping("/{id}")
    public ResponseEntity<RutaDto> actualizar(@PathVariable Long id, @RequestBody RutaDto rutaDto) {

        Ruta rutaActual = servicio.obtenerPorId(id);

        rutaActual.setCantidadDepositos(
                rutaDto.cantidadDepositos() != null ? rutaDto.cantidadDepositos() : rutaActual.getCantidadDepositos());
        rutaActual.setCantidadTramos(
                rutaDto.cantidadTramos() != null ? rutaDto.cantidadTramos() : rutaActual.getCantidadTramos());
        rutaActual.setCostoPorTramo(
                rutaDto.costoPorTramo() != null ? rutaDto.costoPorTramo() : rutaActual.getCostoPorTramo());

        Ruta rutaActualizada = servicio.actualizar(id, rutaActual);

        return ResponseEntity.ok(DtoHandler.convertirRutaDto(rutaActualizada));
    }

    @Operation(summary = "Obtener una Ruta", description = "Obtiene una Ruta dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<RutaDto> obtener(@PathVariable Long id) {
        Ruta ruta = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirRutaDto(ruta));
    }

    @Operation(summary = "Obtener todas las Ruta", description = "Obtiene todas las Rutas")
    @GetMapping
    public ResponseEntity<List<RutaDto>> obtenerTodos() {
        List<Ruta> lista = servicio.listarTodos();
        List<RutaDto> dtos = lista.stream().map(DtoHandler::convertirRutaDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/tentativas")
    public ResponseEntity<?> guardarTentativas(@RequestBody RutaTentativaDtoIn dto) {
        try {
            List<RutaTentativaDtoOut> rutas = servicio.crearRutasTentativas(dto);
            return ResponseEntity.ok(rutas);
        } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }
    }

    @Operation(summary = "Elimina una Ruta", description = "Elimina una Ruta dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
