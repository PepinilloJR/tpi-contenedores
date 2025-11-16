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

import com.commonlib.dto.EstadiaDto;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.servicios.EstadiaServicio;

@RestController
@RequestMapping("/api/depositos/estadias")// Coincide con la documentación de endpoints
public class EstadiaControlador {

    private final EstadiaServicio servicio;

    public EstadiaControlador(EstadiaServicio servicio) {
        this.servicio = servicio;
    }

    // --- Helpers de conversión (Entity <-> DTO) ---

    private EstadiaDto convertirDto(Estadia e) {
        if (e == null) return null;
        return new EstadiaDto(
            e.getIdEstadia(),
            e.getDeposito().getIdDeposito(), // Obtenemos solo el ID del depósito
            e.getIdTramo(),
            e.getFechaHoraEntrada(),
            e.getFechaHoraSalida()
        );
    }

    private Estadia convertirEntidad(EstadiaDto dto) {
        Estadia e = new Estadia();
        // No seteamos el ID del depósito ni el ID de la estadía aquí, 
        // ya que el servicio se encarga de buscar el Deposito.
        e.setIdTramo(dto.idTramo());
        e.setFechaHoraEntrada(dto.fechaHoraEntrada());
        e.setFechaHoraSalida(dto.fechaHoraSalida());
        return e;
    }

    // --- Endpoints CRUD ---

    @PostMapping
    public ResponseEntity<EstadiaDto> crear(@RequestBody EstadiaDto estadiaDto) {
        Estadia estadiaEntidad = convertirEntidad(estadiaDto);
        
        // Llamamos al servicio pasando la entidad y el ID del depósito
        Estadia estadiaCreada = servicio.crear(estadiaEntidad, estadiaDto.idDeposito()); 
        
        return ResponseEntity.status(201).body(convertirDto(estadiaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadiaDto> actualizar(@PathVariable Long id, @RequestBody EstadiaDto estadiaDto) {
        
        // Obtenemos el ID del depósito desde el DTO
        Long idDeposito = estadiaDto.idDeposito();

        // Creamos una entidad temporal para la actualización
        Estadia estadiaActualizar = convertirEntidad(estadiaDto);

        Estadia estadiaActualizada = servicio.actualizar(id, estadiaActualizar, idDeposito);
        return ResponseEntity.ok(convertirDto(estadiaActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadiaDto> obtener(@PathVariable Long id) {
        Estadia estadia = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(estadia));
    }

    @GetMapping
    public ResponseEntity<List<EstadiaDto>> obtenerTodos() {
        List<Estadia> lista = servicio.listarTodos();
        List<EstadiaDto> dtos = lista.stream().map(this::convertirDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}