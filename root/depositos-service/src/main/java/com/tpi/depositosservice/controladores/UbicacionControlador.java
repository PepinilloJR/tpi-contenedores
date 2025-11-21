package com.tpi.depositosservice.controladores;

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

import com.commonlib.dto.DepositoDto;

import com.tpi.depositosservice.servicios.UbicacionServicio;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionControlador {

    private final UbicacionServicio servicio;

    public UbicacionControlador(UbicacionServicio servicio) {
        this.servicio = servicio;
    }

    // --- Helpers de conversión (igual que tu compañero) ---

    private DepositoDto convertirDto(Deposito d) {
        if (d == null)
            return null;
        return new DepositoDto(
                d.getIdDeposito(),
                d.getNombre(),
                d.getDireccion(),
                d.getLatitud(),
                d.getLongitud(),
                d.getCostoEstadia());
    }

    private Deposito convertirEntidad(DepositoDto dto) {
        Deposito d = new Deposito();
        // Ojo: no seteamos el ID al crear
        d.setNombre(dto.nombre());
        d.setDireccion(dto.direccion());
        d.setLatitud(dto.latitud());
        d.setLongitud(dto.longitud());
        d.setCostoEstadia(dto.costoEstadia());
        return d;
    }

    // --- Endpoints CRUD ---

    @Operation(summary = "Crear un Deposito", description = "Crea un nuevo deposito.")
    @PostMapping
    public ResponseEntity<DepositoDto> crear(@RequestBody DepositoDto depositoDto) {
        Deposito depositoEntidad = convertirEntidad(depositoDto);
        Deposito depositoCreado = servicio.crear(depositoEntidad);
        return ResponseEntity.status(201).body(convertirDto(depositoCreado));
    }

    @Operation(summary = "Actualizar un Deposito", description = "Actualiza un deposito dado segun id.")
    @PutMapping("/{id}")
    public ResponseEntity<DepositoDto> actualizar(@PathVariable Long id, @RequestBody DepositoDto depositoDto) {
        // Obtenemos el deposito existente
        Deposito depositoActual = servicio.obtenerPorId(id);

        // Actualizamos los campos
        depositoActual.setNombre(depositoDto.nombre() != null ? depositoDto.nombre() : depositoActual.getNombre());
        depositoActual.setDireccion(
                depositoDto.direccion() != null ? depositoDto.direccion() : depositoActual.getDireccion());
        depositoActual.setLatitud(depositoDto.latitud() != null ? depositoDto.latitud() : depositoActual.getLatitud());
        depositoActual
                .setLongitud(depositoDto.longitud() != null ? depositoDto.longitud() : depositoActual.getLongitud());
        depositoActual.setCostoEstadia(
                depositoDto.costoEstadia() != null ? depositoDto.costoEstadia() : depositoActual.getCostoEstadia());

        Deposito depositoActualizado = servicio.actualizar(id, depositoActual);
        return ResponseEntity.ok(convertirDto(depositoActualizado));
    }

    @Operation(summary = "Obtener un Deposito", description = "Obtiene un deposito dado segun id.")
    @GetMapping("/{id}")
    public ResponseEntity<DepositoDto> obtener(@PathVariable Long id) {
        Deposito deposito = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(deposito));
    }

    @Operation(summary = "Obtiene todos los Depositos", description = "Obtiene todos los Depositos")
    @GetMapping
    public ResponseEntity<List<DepositoDto>> obtenerTodos() {
        List<Deposito> lista = servicio.listarTodos();
        List<DepositoDto> dtos = lista.stream().map(this::convertirDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar un Deposito", description = "Elimina un deposito dado segun id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}