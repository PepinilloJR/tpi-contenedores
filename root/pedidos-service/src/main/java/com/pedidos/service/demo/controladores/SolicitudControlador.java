package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.dto.SolicitudDtoCreacion;
import com.pedidos.service.demo.dto.SolicitudDtoIn;
import com.pedidos.service.demo.servicios.SolicitudServicio;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commonlib.entidades.Solicitud;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudControlador {
    
    private final SolicitudServicio solicitudServicio;

    @Operation(summary = "Crear una Solicitud", description = "Crea una nueva solicitud con estado BORRADOR")
    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody SolicitudDtoCreacion solicitud) {
        Solicitud nuevaSolicitud = solicitudServicio.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }

    @Operation(summary = "Actualizar una Solicitud", description = "Actualiza el estado, costos de una solicitud. Registra cambios de estado en el seguimiento")
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id, @RequestBody SolicitudDtoIn datos) {
        Solicitud solicitudActualizada = solicitudServicio.actualizar(id, datos);
        return ResponseEntity.ok(solicitudActualizada);
    }

    @Operation(summary = "Obtener una Solicitud", description = "Obtiene una solicitud por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudServicio.obtenerPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    @Operation(summary = "Obtener solicitudes por cliente", description = "Obtiene todas las solicitudes de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Solicitud>> obtenerPorCliente(@PathVariable Long clienteId) {
        List<Solicitud> solicitudes = solicitudServicio.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(solicitudes);
    }

    @Operation(summary = "Obtener todas las Solicitudes", description = "Lista todas las solicitudes del sistema")
    @GetMapping
    public ResponseEntity<List<Solicitud>> listarTodas() {
        List<Solicitud> solicitudes = solicitudServicio.listarTodos();
        return ResponseEntity.ok(solicitudes);
    }

    @Operation(summary = "Eliminar una Solicitud", description = "Elimina una solicitud por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}