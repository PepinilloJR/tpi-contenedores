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
import com.commonlib.entidades.Tarifa;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/tarifas")
public class TarifaControlador {

    private final TarifaServicio servicio;

    public TarifaControlador(TarifaServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una Tarifa", description = "Crea una Tarifa")
    @PostMapping
    public ResponseEntity<TarifaDto> crear(@RequestBody TarifaDto dto) {

        Tarifa entity = DtoHandler.convertirTarifaEntidad(dto);

        Tarifa creada = servicio.crear(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DtoHandler.convertirTarifaDto(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifaDto> actualizar(
            @PathVariable Long id,
            @RequestBody TarifaDto dto) {

        Tarifa actual = servicio.obtenerPorId(id);

        // merge parcial
        if (dto.costoKilometro() != null)
            actual.setCostoKilometro(dto.costoKilometro());

        if (dto.costoVolumen() != null)
            actual.setCostoVolumen(dto.costoVolumen());

        Tarifa actualizada = servicio.actualizar(id, actual);

        return ResponseEntity.ok(DtoHandler.convertirTarifaDto(actualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifaDto> obtener(@PathVariable Long id) {
        Tarifa tarifa = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirTarifaDto(tarifa));
    }

    @GetMapping
    public ResponseEntity<List<TarifaDto>> obtenerTodos() {
        List<Tarifa> lista = servicio.listarTodos();
        List<TarifaDto> dtos = lista.stream()
                .map(DtoHandler::convertirTarifaDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
