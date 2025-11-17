package com.pedidos.service.demo.controladores;

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

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.TramoDto;
import com.commonlib.entidades.Tramo;
import com.pedidos.service.demo.servicios.RutaServicio;
import com.pedidos.service.demo.servicios.TramoServicio;

/*
!!!!!!!!!
!!!!!!!!!
 * Repasar todos los servicios y ver las reglas!!!!!!!!!
 * Tambien en los dto usar los validate!!!!!!!!!
!!!!!!!!!
!!!!!!!!!
!!!!!!!!!
!!!!!!!!!
 */

@RestController
@RequestMapping("/api/tramos")
public class TramoControlador {

    private final RutaServicio rutaServicio;
    private final TramoServicio servicio;

    public TramoControlador(TramoServicio servicio, RutaServicio rutaServicio) {
        this.servicio = servicio;
        this.rutaServicio = rutaServicio;
    }

    // Maybe validate
    @PostMapping
    public ResponseEntity<TramoDto> crear(@RequestBody TramoDto tramoDto) {
        System.out.println();

        Tramo tramoEntidad = DtoHandler.convertirTramoEntidad(tramoDto);
        var rutaId = tramoDto.ruta().id();
        var rutaBd = rutaServicio.obtenerPorId(rutaId);
        tramoEntidad.setRuta(rutaBd);
        Tramo tramoCreado = servicio.crear(tramoEntidad);

        return ResponseEntity.status(201).body(DtoHandler.convertirTramoDto(tramoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TramoDto> actualizar(@PathVariable Long id, @RequestBody TramoDto tramoDto) {
        // Soporta la actualizacion parcial, y hay que ver reglas en el servicio
        Tramo tramoActual = servicio.obtenerPorId(id);
        tramoActual.setEstado(tramoDto.estado() != null ? tramoDto.estado() : tramoActual.getEstado());
        tramoActual.setFechaHoraFin(
                tramoDto.fechaHoraFin() != null ? tramoDto.fechaHoraFin() : tramoActual.getFechaHoraFin());
        if (tramoDto.camion() != null) {
            tramoActual.setCamion(DtoHandler.convertirCamionEntidad(tramoDto.camion()));
        }
        Tramo tramoActualizado = servicio.actualizar(id, tramoActual);

        return ResponseEntity.ok(DtoHandler.convertirTramoDto(tramoActualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TramoDto> obtener(@PathVariable Long id) {
        Tramo tramo = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirTramoDto(tramo));
    }

    @GetMapping("/transportista/{transportista}")
    public ResponseEntity<List<TramoDto>> obtener(@PathVariable String transportista) {
        List<Tramo> tramos = servicio.obtenerPorTransportista(transportista);
        return ResponseEntity.ok(DtoHandler.convertirTramosDto(tramos));
    }

    // por ejemplo -> GET /api/tramos?idRuta=5

    @GetMapping
    public ResponseEntity<List<TramoDto>> obtenerTodos(@RequestParam(required = false) Long rutaId) {
        List<Tramo> lista;
        if (rutaId != null) {
            lista = servicio.obtenerPorIdRuta(rutaId);
        } else {
            lista = servicio.listarTodos();
        }
        List<TramoDto> dtos = lista.stream().map(DtoHandler::convertirTramoDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
