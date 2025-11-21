package com.camiones.service.demo.controladores;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camiones.service.demo.servicios.TarifaServicio;
import com.camiones.service.demo.dto.DtoHandler;
import com.camiones.service.demo.dto.TarifaDto;
import com.camiones.service.demo.exepciones.ResourceNotFoundException;
import com.commonlib.entidades.Tarifa;
import com.commonlib.error.ErrorRequest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaControlador {

    private final TarifaServicio servicio;

    public TarifaControlador(TarifaServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una Tarifa", description = "Crea una Tarifa")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TarifaDto dto) {
        try {
            Tarifa entity = DtoHandler.convertirTarifaEntidad(dto);
            Tarifa creada = servicio.crear(entity);
            return ResponseEntity.status(201).body(DtoHandler.convertirTarifaDto(creada));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorRequest(400, "No se pudo crear la tarifa: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorRequest(500, "Error interno al crear la tarifa"));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TarifaDto dto) {
        try {
            Tarifa actual = servicio.obtenerPorId(id); // ResourceNotFoundException si no existe

            // merge parcial (como tenías antes)
            if (dto.costoKilometro() != null)
                actual.setCostoKilometro(dto.costoKilometro());
            if (dto.costoVolumen() != null)
                actual.setCostoVolumen(dto.costoVolumen());

            Tarifa actualizada = servicio.actualizar(id, actual); // puede lanzar IllegalArgumentException
            return ResponseEntity.ok(DtoHandler.convertirTarifaDto(actualizada));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorRequest(404, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorRequest(500, "Error interno al actualizar la tarifa"));
        }
    }

    @Operation(summary = "Obtener una Tarifa", description = "Obtener una Tarifa dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            Tarifa tarifa = servicio.obtenerPorId(id);
            return ResponseEntity.ok(DtoHandler.convertirTarifaDto(tarifa));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorRequest(404, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorRequest(500, "Error interno al obtener la tarifa"));
        }

    }

    @Operation(summary = "Obtener todas las Tarifas", description = "Obtiene todas las Tarifas")
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Tarifa> lista = servicio.listarTodos();
            List<TarifaDto> dtos = lista.stream()
                    .map(DtoHandler::convertirTarifaDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorRequest(500, "Error interno al listar las tarifas"));
        }

    }

    @Operation(summary = "Eliminar una Tarifa", description = "Elimina una Tarifa dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            servicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorRequest(500, "Error interno al eliminar la tarifa"));
        }

    }
}
