package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.servicios.ContenedorServicio;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import com.commonlib.dto.ContenedorDto;
import com.commonlib.entidades.Contenedor;
import com.commonlib.error.ErrorRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.pedidos.service.demo.dto.ContenedorDtoIn;
import com.pedidos.service.demo.dto.ContenedorDtoOut;
import com.pedidos.service.demo.dto.DtoHandler;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorControlador {
    private final ContenedorServicio servicio;

    public ContenedorControlador(ContenedorServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear un Contenedor", description = "Crea un Contenedor")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ContenedorDtoIn contenedorDto) {
        Contenedor contenedorCreado;
        try {
            contenedorCreado = servicio.crear(contenedorDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));

        }

        ContenedorDtoOut contenedorDtoOut = new ContenedorDtoOut(contenedorCreado.getId(), 
            contenedorCreado.getPeso(), 
            contenedorCreado.getVolumen(), 
            contenedorCreado.getEstado(), 
            contenedorCreado.getUbicacion() != null ? contenedorCreado.getUbicacion().getId() : null);
        
        return ResponseEntity.status(201).body(contenedorDtoOut);
    }

    // Primero en corregir

    // En el servicio hay que controlar el costoVolumen
    @Operation(summary = "Actualizar un Contenedor", description = "Actualiza un Contenedor dado segun id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ContenedorDtoIn contenedorDtoIn) {
        Contenedor contenedorActualizado;
        try {
            contenedorActualizado = servicio.actualizar(id, contenedorDtoIn);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }

        ContenedorDtoOut contenedorDtoOut = new ContenedorDtoOut(contenedorActualizado.getId()
        , contenedorActualizado.getPeso(), contenedorActualizado.getVolumen(), contenedorActualizado.getEstado(), 
        contenedorActualizado.getUbicacion() != null ? contenedorActualizado.getUbicacion().getId() : null);
        
        return ResponseEntity.ok(contenedorDtoOut);
    }



    @Operation(summary = "Obtener un Contenedor", description = "Obtener un Contenedor dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDto> obtener(@PathVariable Long id) {
        Contenedor contenedor = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirContenedorDto(contenedor));
    }

    @Operation(summary = "Obtener todos los Contenedores", description = "Obtiene todos los contenedores")
    @GetMapping
    public ResponseEntity<List<ContenedorDto>> obtenerTodos() {
        List<Contenedor> lista = servicio.listarTodos();
        List<ContenedorDto> dtos = lista.stream().map(DtoHandler::convertirContenedorDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Hay que controlar los estados

    @Operation(summary = "Obtener un Contenedor segun su estado", description = "Obtiene un Contenedor segun su estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ContenedorDto> obtenerPorEstado(@PathVariable String estado) {
        Contenedor contenedorSegunEstado = servicio.obtenerPorEstado(estado);
        return ResponseEntity.ok(DtoHandler.convertirContenedorDto(contenedorSegunEstado));
    }

    @Operation(summary = "Obtener todos los Contenedores pendientes", description = "Obtiene todos los Contenedores en estado pendiente")
    @GetMapping("/pendientes")
    public ResponseEntity<List<ContenedorDto>> obtenerPendientes() {
        List<Contenedor> pendientes = servicio.listarPendientes();
        List<ContenedorDto> dtos = pendientes.stream().map(DtoHandler::convertirContenedorDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar un Contenedor", description = "Elimina un Contenedor dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
