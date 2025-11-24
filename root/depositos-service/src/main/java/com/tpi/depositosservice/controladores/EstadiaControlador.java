package com.tpi.depositosservice.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.dto.EstadiaDtoIn;
import com.tpi.depositosservice.dto.EstadiaDtoOut;
import com.tpi.depositosservice.dto.DtoHandler;
import com.tpi.depositosservice.servicios.EstadiaServicio;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estadias")
@RequiredArgsConstructor
public class EstadiaControlador {

    private final EstadiaServicio estadiaServicio;

    // Esto lo puedo sacar
    @Operation(summary = "Crear una estadía", description = "Crea una nueva estadía entre dos tramos consecutivos en un depósito")
    @PostMapping
    public ResponseEntity<EstadiaDtoOut> crear(@RequestBody EstadiaDtoIn datos) {
        Estadia estadiaCreada = estadiaServicio.crear(datos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoHandler.convertirEstadiaDtoOut(estadiaCreada));
    }

    

    @Operation(summary = "Obtener una estadía", description = "Obtiene una estadía por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EstadiaDtoOut> obtenerPorId(@PathVariable Long id) {
        Estadia estadia = estadiaServicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirEstadiaDtoOut(estadia));
    }

    @Operation(summary = "Obtener todas las estadías", description = "Lista todas las estadías del sistema")
    @GetMapping
    public ResponseEntity<List<EstadiaDtoOut>> listarTodas() {
        List<Estadia> estadias = estadiaServicio.listarTodos();
        List<EstadiaDtoOut> dtos = estadias.stream()
                .map(DtoHandler::convertirEstadiaDtoOut)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener todas las estadías", description = "Lista todas las estadías del sistema")
    @GetMapping("/contenedor/{idContenedor}")
    public ResponseEntity<List<EstadiaDtoOut>> listarTodasPorContenedor(@PathVariable Long idContenedor) {
        List<Estadia> estadias = estadiaServicio.obtenerEstadiasPorContenedor(idContenedor);
        List<EstadiaDtoOut> dtos = estadias.stream()
                .map(DtoHandler::convertirEstadiaDtoOut)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar una estadía", description = "Elimina una estadía por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadiaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}