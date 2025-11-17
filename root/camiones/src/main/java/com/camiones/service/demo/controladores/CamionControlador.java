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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camiones.service.demo.servicios.CamionServicio;
import com.commonlib.dto.CamionDto;
import com.commonlib.entidades.Camion;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/camiones")
public class CamionControlador {

    private final CamionServicio servicio;

    public CamionControlador(CamionServicio servicio) {
        this.servicio = servicio;
    }

    // helper: entidad -> dto
    private CamionDto convertirDto(Camion c) {
        if (c == null)
            return null;
        return new CamionDto(
                c.getId(),
                c.getPatente(),
                c.getNombreTransportista(),
                c.getTelefono(),
                c.getCapacidadPesoKg(),
                c.getCapacidadVolumenM3(),
                c.getCostoPorKm(),
                c.getConsumoCombustibleLx100km(),
                c.getDisponible() // <-- fix aquí
        );
    }

    // helper: dto -> entidad (para crear)
    private Camion convertirEntidad(CamionDto dto) {
        Camion c = new Camion();
        c.setPatente(dto.patente());
        c.setNombreTransportista(dto.nombreTransportista());
        c.setTelefono(dto.telefono());
        c.setCapacidadPesoKg(dto.capacidadPesoKg());
        c.setCapacidadVolumenM3(dto.capacidadVolumenM3());
        c.setCostoPorKm(dto.costoPorKm());
        c.setConsumoCombustibleLx100km(dto.consumoCombustibleLx100km());
        c.setDisponible(dto.disponible() != null ? dto.disponible() : true);
        return c;
    }

    @Operation(summary = "Crear un Camion", description = "Crea un Camion")
    @PostMapping
    public ResponseEntity<CamionDto> crear(@Valid @RequestBody CamionDto dto) {
        Camion entity = convertirEntidad(dto);
        Camion creado = servicio.crear(entity);
        return ResponseEntity.status(201).body(convertirDto(creado));
    }

    @Operation(summary = "Actualizar un Camion", description = "Actualiza un Camion dado segun id")
    @PutMapping("/{id}")
    public ResponseEntity<CamionDto> actualizar(@PathVariable Long id, @RequestBody CamionDto dto) {
        // actualización parcial: aplica solo campos no nulos
        Camion actual = servicio.obtenerPorId(id);

        if (dto.patente() != null)
            actual.setPatente(dto.patente());
        if (dto.nombreTransportista() != null)
            actual.setNombreTransportista(dto.nombreTransportista());
        if (dto.telefono() != null)
            actual.setTelefono(dto.telefono());
        if (dto.capacidadPesoKg() != null)
            actual.setCapacidadPesoKg(dto.capacidadPesoKg());
        if (dto.capacidadVolumenM3() != null)
            actual.setCapacidadVolumenM3(dto.capacidadVolumenM3());
        if (dto.costoPorKm() != null)
            actual.setCostoPorKm(dto.costoPorKm());
        if (dto.consumoCombustibleLx100km() != null)
            actual.setConsumoCombustibleLx100km(dto.consumoCombustibleLx100km());
        if (dto.disponible() != null)
            actual.setDisponible(dto.disponible());

        Camion actualizado = servicio.actualizar(id, actual);
        return ResponseEntity.ok(convertirDto(actualizado));
    }

    @Operation(summary = "Obtener un Camion", description = "Obtener un Camion dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<CamionDto> obtener(@PathVariable Long id) {
        Camion camion = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(camion));
    }

    @Operation(summary = "Obtener todos los Camiones", description = "Obtiene todos los Camiones")
    @GetMapping
    public ResponseEntity<List<CamionDto>> obtenerTodos() {
        List<Camion> lista = servicio.listarTodos();
        List<CamionDto> dtos = lista.stream().map(this::convertirDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtiene un Camion disponible", description = "Obtener un Camion disponible")
    @GetMapping("/disponible")
    public ResponseEntity<CamionDto> obtenerDisponible() {
        Camion camionDisponible = servicio.obtenerDisponible();
        return ResponseEntity.ok(convertirDto(camionDisponible));
    }

    @Operation(summary = "Obtiene un Camion segun si esta disponible y capacidad", description = "Obtiene un Camion dado segun segun disponibildiad y capacidad")
    @GetMapping("/disponible/por-capacidad")
    public ResponseEntity<CamionDto> obtenerDisponiblePorCapacidad(@RequestParam(required = true) Double peso,
            @RequestParam(required = true) Double volumen) {

        Camion camionDisponible = servicio.obtenerDisponiblePorCapacidad(peso, volumen);
        return ResponseEntity.ok(convertirDto(camionDisponible));
    }

    @Operation(summary = "Eliminar un Camion", description = "Elimina un Camion dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
