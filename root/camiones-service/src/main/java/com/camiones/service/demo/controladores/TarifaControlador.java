package com.camiones.service.demo.controladores;

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

import com.camiones.service.demo.servicios.TarifaServicio;
import com.commonlib.dto.TarifaDto;
import com.commonlib.entidades.Tarifa;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaControlador {

    private final TarifaServicio servicio;

    public TarifaControlador(TarifaServicio servicio) {
        this.servicio = servicio;
    }

    // helper: entidad -> dto
    private TarifaDto convertirDto(Tarifa t) {
        if (t == null) {
            return null;
        }
        return new TarifaDto(
                t.getId(),
                t.getCostoLitro(),
                t.getCostoVolumen(),
                t.getCostoKilometro());
    }

    // helper: dto -> entidad (para crear)
    private Tarifa convertirEntidad(TarifaDto dto) {
        Tarifa t = new Tarifa();
        t.setCostoKilometro(dto.costoLitro());
        t.setCostoVolumen(dto.costoVolumen());
        t.setCostoKilometro(dto.costoKilometro());
        return t;
    }

    @Operation(summary = "Crear una Tarifa", description = "Crea una Tarifa")
    @PostMapping
    public ResponseEntity<TarifaDto> crear(@Valid @RequestBody TarifaDto dto) {
        Tarifa entity = convertirEntidad(dto);
        Tarifa creada = servicio.crear(entity);
        return ResponseEntity.status(201).body(convertirDto(creada));
    }

    @Operation(summary = "Actualizar una Tarifa", description = "Actualiza una Tarifa dada segun id")
    @PutMapping("/{id}")
    public ResponseEntity<TarifaDto> actualizar(@PathVariable Long id, @RequestBody TarifaDto dto) {
        // actualización parcial: aplica solo campos no nulos
        Tarifa actual = servicio.obtenerPorId(id);

        actual.setCostoKilometro(dto.costoKilometro() != null ? dto.costoKilometro() : actual.getCostoKilometro());
        actual.setCostoLitro(dto.costoLitro() != null ? dto.costoLitro() : actual.getCostoLitro());
        actual.setCostoVolumen(dto.costoVolumen() != null ? dto.costoVolumen() : actual.getCostoVolumen());


        Tarifa actualizada = servicio.actualizar(id, actual);
        return ResponseEntity.ok(convertirDto(actualizada));
    }

    @Operation(summary = "Obtener una Tarifa", description = "Obtener una Tarifa dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<TarifaDto> obtener(@PathVariable Long id) {
        Tarifa tarifa = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(tarifa));
    }

    @Operation(summary = "Obtener todas las Tarifas", description = "Obtiene todas las Tarifas")
    @GetMapping
    public ResponseEntity<List<TarifaDto>> obtenerTodos() {
        List<Tarifa> lista = servicio.listarTodos();
        List<TarifaDto> dtos = lista.stream()
                .map(this::convertirDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar una Tarifa", description = "Elimina una Tarifa dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
