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
                t.getNombre(),
                t.getPrecioPorKm(),
                t.getPrecioFijo(),
                t.getMoneda(),
                t.getVigenciaDesde(),
                t.getVigenciaHasta(),
                t.getActivo()
        );
    }

    // helper: dto -> entidad (para crear)
    private Tarifa convertirEntidad(TarifaDto dto) {
        Tarifa t = new Tarifa();
        t.setNombre(dto.nombre());
        t.setPrecioPorKm(dto.precioPorKm());
        t.setPrecioFijo(dto.precioFijo());
        t.setMoneda(dto.moneda());
        t.setVigenciaDesde(dto.vigenciaDesde());
        t.setVigenciaHasta(dto.vigenciaHasta());
        t.setActivo(dto.activo() != null ? dto.activo() : true);
        return t;
    }

    @PostMapping
    public ResponseEntity<TarifaDto> crear(@Valid @RequestBody TarifaDto dto) {
        Tarifa entity = convertirEntidad(dto);
        Tarifa creada = servicio.crear(entity);
        return ResponseEntity.status(201).body(convertirDto(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifaDto> actualizar(@PathVariable Long id, @RequestBody TarifaDto dto) {
        // actualización parcial: aplica solo campos no nulos
        Tarifa actual = servicio.obtenerPorId(id);

        if (dto.nombre() != null) {
            actual.setNombre(dto.nombre());
        }
        if (dto.precioPorKm() != null) {
            actual.setPrecioPorKm(dto.precioPorKm());
        }
        if (dto.precioFijo() != null) {
            actual.setPrecioFijo(dto.precioFijo());
        }
        if (dto.moneda() != null) {
            actual.setMoneda(dto.moneda());
        }
        if (dto.vigenciaDesde() != null) {
            actual.setVigenciaDesde(dto.vigenciaDesde());
        }
        if (dto.vigenciaHasta() != null) {
            actual.setVigenciaHasta(dto.vigenciaHasta());
        }
        if (dto.activo() != null) {
            actual.setActivo(dto.activo());
        }

        Tarifa actualizada = servicio.actualizar(id, actual);
        return ResponseEntity.ok(convertirDto(actualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifaDto> obtener(@PathVariable Long id) {
        Tarifa tarifa = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(tarifa));
    }

    @GetMapping
    public ResponseEntity<List<TarifaDto>> obtenerTodos() {
        List<Tarifa> lista = servicio.listarTodos();
        List<TarifaDto> dtos = lista.stream()
                .map(this::convertirDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
