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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camiones.service.demo.dto.CamionDto;
import com.camiones.service.demo.dto.DtoHandler;
import com.camiones.service.demo.servicios.CamionServicio;
import com.camiones.service.demo.servicios.TarifaServicio;
import com.commonlib.entidades.Camion;
import com.commonlib.entidades.Tarifa;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/camiones")
public class CamionControlador {

    private final CamionServicio servicio;
    private final TarifaServicio servicioTarifa;

    public CamionControlador(CamionServicio servicio, TarifaServicio servicioTarifa) {
        this.servicio = servicio;
        this.servicioTarifa = servicioTarifa;
    }

    @Operation(summary = "Crear un Camion", description = "Crea un Camion")
    @PostMapping
    public ResponseEntity<CamionDto> crear(@RequestBody CamionDto dto) {
        Camion entity = DtoHandler.convertirCamionEntidad(dto);

        if (dto.idTarifa() != null) {
            Tarifa tarifa = servicioTarifa.obtenerPorId(dto.idTarifa());
            entity.setTarifa(tarifa);
        }

        Camion creado = servicio.crear(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoHandler.convertirCamionDto(creado));
    }

    @Operation(summary = "Actualizar un Camion", description = "Actualiza un Camion dado segun id")
    @PutMapping("/{id}")
    public ResponseEntity<CamionDto> actualizar(@PathVariable Long id, @RequestBody CamionDto dto) {

        Camion actual = servicio.obtenerPorId(id);

        if (dto.idTarifa() != null) {
            Tarifa tarifa = servicioTarifa.obtenerPorId(dto.idTarifa());
            actual.setTarifa(tarifa);
        }

        if (dto.patente() != null) actual.setPatente(dto.patente());
        if (dto.nombreTransportista() != null) actual.setNombreTransportista(dto.nombreTransportista());
        if (dto.telefonoTransportista() != null) actual.setTelefonoTransportista(dto.telefonoTransportista());
        if (dto.capacidadPeso() != null) actual.setCapacidadPeso(dto.capacidadPeso());
        if (dto.capacidadVolumen() != null) actual.setCapacidadVolumen(dto.capacidadVolumen());
        if (dto.consumoCombustiblePromedio() != null) actual.setConsumoCombustiblePromedio(dto.consumoCombustiblePromedio());
        if (dto.disponible() != null) actual.setDisponible(dto.disponible());

        Camion actualizado = servicio.actualizar(id, actual);
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(actualizado));
    }

    @Operation(summary = "Obtener un Camion", description = "Obtener un Camion dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<CamionDto> obtener(@PathVariable Long id) {
        Camion camion = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(camion));
    }

    @Operation(summary = "Obtener todos los Camiones", description = "Obtiene todos los Camiones")
    @GetMapping
    public ResponseEntity<List<CamionDto>> obtenerTodos() {
        List<Camion> lista = servicio.listarTodos();
        List<CamionDto> dtos = lista.stream().map(DtoHandler::convertirCamionDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtiene un Camion disponible", description = "Obtener un Camion disponible")
    @GetMapping("/disponible")
    public ResponseEntity<CamionDto> obtenerDisponible() {
        Camion camionDisponible = servicio.obtenerDisponible();
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(camionDisponible));
    }

    @Operation(summary = "Obtiene un Camion segun capacidad", description = "Obtiene un Camion disponible que soporte peso y volumen")
    @GetMapping("/disponible/por-capacidad")
    public ResponseEntity<CamionDto> obtenerDisponiblePorCapacidad(
            @RequestParam Double peso,
            @RequestParam Double volumen) {

        Camion camionDisponible = servicio.obtenerDisponiblePorCapacidad(peso, volumen);
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(camionDisponible));
    }

    @Operation(summary = "Eliminar un Camion", description = "Elimina un Camion dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    /* =====================================================================
     *                     NUEVOS ENDPOINTS: OCUPAR / LIBERAR
     * ===================================================================== */

    @Operation(summary = "Marcar camión como ocupado", description = "El tramo lo está utilizando")
    @PutMapping("/{id}/ocupar")
    public ResponseEntity<CamionDto> ocupar(@PathVariable Long id) {
        Camion camion = servicio.marcarComoOcupado(id);
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(camion));
    }

    @Operation(summary = "Liberar camión", description = "Lo deja disponible para nuevos tramos")
    @PutMapping("/{id}/liberar")
    public ResponseEntity<CamionDto> liberar(@PathVariable Long id) {
        Camion camion = servicio.liberar(id);
        return ResponseEntity.ok(DtoHandler.convertirCamionDto(camion));
    }
}
