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

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.EstadiaDto;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.servicios.EstadiaServicio;

@RestController
@RequestMapping("/api/estadias")// Coincide con la documentación de endpoints
public class EstadiaControlador {


    private final EstadiaServicio servicio;

    public EstadiaControlador(EstadiaServicio servicio) {
        this.servicio = servicio;
    }

    // --- Helpers de conversión (Entity <-> DTO) ---



    // --- Endpoints CRUD ---

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EstadiaDto estadiaDto) {

    
        Estadia estadiaEntidad = DtoHandler.convertirEstadiaEntidad(estadiaDto);
        estadiaEntidad.setTramo(servicio.obtener(estadiaDto.tramo().id()));
        // Llamamos al servicio pasando la entidad y el ID del depósito
        if (!estadiaEntidad.getTramo().getOrigen().getTipo().toLowerCase().equals("deposito"))
        {
            return ResponseEntity.status(400).body(
                "el origen del tramo no es un deposito (una estadia se define al origen de un tramo y solo cuenta si este es un deposito)" + estadiaEntidad.getTramo().getOrigen()
                );
        }
        Estadia estadiaCreada = servicio.crear(estadiaEntidad); 
        
        return ResponseEntity.status(201).body(DtoHandler.convertirEstadiaDto(estadiaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadiaDto> actualizar(@PathVariable Long id, @RequestBody EstadiaDto estadiaDto) {
        
        // Obtenemos el ID del depósito desde el DTO
        //Long idDeposito = estadiaDto.idDeposito();

        // Creamos una entidad temporal para la actualización
        Estadia estadiaActualizar = servicio.obtenerPorId(id);
        estadiaActualizar.setTramo(DtoHandler.convertirTramoEntidad(estadiaDto.tramo()));
        estadiaActualizar.setFechaHoraEntrada(estadiaDto.fechaHoraEntrada());
        estadiaActualizar.setFechaHoraSalida(estadiaDto.fechaHoraSalida());

        Estadia estadiaActualizada = servicio.actualizar(estadiaActualizar);
        return ResponseEntity.ok(DtoHandler.convertirEstadiaDto(estadiaActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadiaDto> obtener(@PathVariable Long id) {
        Estadia estadia = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirEstadiaDto(estadia));
    }

    @GetMapping
    public ResponseEntity<List<EstadiaDto>> obtenerTodos() {
        List<Estadia> lista = servicio.listarTodos();
        List<EstadiaDto> dtos = lista.stream().map(DtoHandler::convertirEstadiaDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}