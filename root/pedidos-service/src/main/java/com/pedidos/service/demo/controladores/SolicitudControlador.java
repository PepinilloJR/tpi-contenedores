package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.dto.SolicitudDtoCreacion;
import com.pedidos.service.demo.dto.SolicitudDtoIn;
import com.pedidos.service.demo.dto.SolicitudDtoOut;
import com.pedidos.service.demo.dto.DtoHandler;
import com.pedidos.service.demo.servicios.SolicitudServicio;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<SolicitudDtoOut> crear(@RequestBody SolicitudDtoCreacion solicitud) {
        Solicitud nuevaSolicitud = solicitudServicio.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(DtoHandler.convertirSolicitudDtoOut(nuevaSolicitud));
    }

    @Operation(summary = "Actualizar una Solicitud", description = "Actualiza el estado, costos de una solicitud. Registra cambios de estado en el seguimiento")
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDtoOut> actualizar(@PathVariable Long id, @RequestBody SolicitudDtoIn datos) {
        Solicitud solicitudActualizada = solicitudServicio.actualizar(id, datos);
        return ResponseEntity.ok(DtoHandler.convertirSolicitudDtoOut(solicitudActualizada));
    }

    @Operation(summary = "Obtener una Solicitud", description = "Obtiene una solicitud por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDtoOut> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudServicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirSolicitudDtoOut(solicitud));
    }

    @Operation(summary = "Obtener solicitudes por cliente", description = "Obtiene todas las solicitudes de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SolicitudDtoOut>> obtenerPorCliente(@PathVariable Long clienteId) {
        List<Solicitud> solicitudes = solicitudServicio.obtenerPorClienteId(clienteId);
        List<SolicitudDtoOut> dtos = solicitudes.stream()
            .map(DtoHandler::convertirSolicitudDtoOut)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener todas las Solicitudes", description = "Lista todas las solicitudes del sistema")
    @GetMapping
    public ResponseEntity<List<SolicitudDtoOut>> listarTodas() {
        List<Solicitud> solicitudes = solicitudServicio.listarTodos();
        List<SolicitudDtoOut> dtos = solicitudes.stream()
            .map(DtoHandler::convertirSolicitudDtoOut)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar una Solicitud", description = "Elimina una solicitud por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}