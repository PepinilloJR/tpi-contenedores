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

import com.commonlib.Enums.EstadosContenedor;
import com.commonlib.entidades.Contenedor;
import com.pedidos.service.demo.dto.ContenedorDtoIn;
import com.pedidos.service.demo.dto.ContenedorDtoOut;
import com.pedidos.service.demo.servicios.ContenedorServicio;

import io.swagger.v3.oas.annotations.Operation;

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

        contenedorCreado = servicio.crear(contenedorDto);

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

        contenedorActualizado = servicio.actualizar(id, contenedorDtoIn);

        ContenedorDtoOut contenedorDtoOut = new ContenedorDtoOut(contenedorActualizado.getId(),
                contenedorActualizado.getPeso(), contenedorActualizado.getVolumen(), contenedorActualizado.getEstado(),
                contenedorActualizado.getUbicacion() != null ? contenedorActualizado.getUbicacion().getId() : null);

        return ResponseEntity.ok(contenedorDtoOut);
    }


    @Operation(summary = "Obtener un Contenedor", description = "Obtener un Contenedor dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Contenedor contenedor;

        contenedor = servicio.obtenerPorId(id);

        ContenedorDtoOut contenedorDtoOut = new ContenedorDtoOut(
                contenedor.getId(), contenedor.getPeso(), contenedor.getVolumen(),
                contenedor.getEstado(),
                contenedor.getUbicacion() != null ? contenedor.getUbicacion().getId() : null);
        return ResponseEntity.ok().body(contenedorDtoOut);
    }

    

    @Operation(summary = "Obtener todos los Contenedores", description = "Obtiene todos los contenedores")
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<Contenedor> lista = servicio.listarTodos();
        List<ContenedorDtoOut> dtos = lista.stream().map((c) -> {
            return new ContenedorDtoOut(
                    c.getId(), c.getPeso(), c.getVolumen(),
                    c.getEstado(),
                    c.getUbicacion() != null ? c.getUbicacion().getId() : null);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Hay que controlar los estados

    @Operation(summary = "Obtener un Contenedor segun su estado", description = "Obtiene un Contenedor segun su estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable EstadosContenedor estado) {
        Contenedor contenedorSegunEstado;
        
        contenedorSegunEstado = servicio.obtenerPorEstado(estado);

        ContenedorDtoOut contenedorDtoOut = new ContenedorDtoOut(
                contenedorSegunEstado.getId(), contenedorSegunEstado.getPeso(), contenedorSegunEstado.getVolumen(),
                contenedorSegunEstado.getEstado(),
                contenedorSegunEstado.getUbicacion() != null ? contenedorSegunEstado.getUbicacion().getId() : null);
        return ResponseEntity.ok(contenedorDtoOut);
    }

    @Operation(summary = "Obtener todos los Contenedores pendientes", description = "Obtiene todos los Contenedores en estado pendiente")
    @GetMapping("/pendientes")
    public ResponseEntity<List<ContenedorDtoOut>> obtenerPendientes() {

        List<Contenedor> pendientes = servicio.listarPendientes();
        List<ContenedorDtoOut> dtos = pendientes.stream().map((c) -> {
            return new ContenedorDtoOut(
                    c.getId(), c.getPeso(), c.getVolumen(),
                    c.getEstado(),
                    c.getUbicacion() != null ? c.getUbicacion().getId() : null);
        })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar un Contenedor", description = "Elimina un Contenedor dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        
        servicio.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}
