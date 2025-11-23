package com.pedidos.service.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.commonlib.entidades.Tramo;
import com.commonlib.error.ErrorRequest;
import com.pedidos.service.demo.dto.TramoDtoIn;
import com.pedidos.service.demo.dto.TramoDtoOut;
import com.pedidos.service.demo.exepciones.ConflictException;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.servicios.RutaServicio;
import com.pedidos.service.demo.servicios.TramoServicio;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/tramos")
public class TramoControlador {

    private final RutaServicio rutaServicio;
    private final TramoServicio servicio;

    public TramoControlador(TramoServicio servicio, RutaServicio rutaServicio) {
        this.servicio = servicio;
        this.rutaServicio = rutaServicio;
    }

    /*
     * // Maybe validate
     * 
     * @Operation(summary = "Crear un Tramo", description = "Crea un Tramo")
     * 
     * @PostMapping
     * public ResponseEntity<TramoDto> crear(@RequestBody TramoDto tramoDto) {
     * System.out.println();
     * 
     * Tramo tramoEntidad = DtoHandler.convertirTramoEntidad(tramoDto);
     * var rutaId = tramoDto.ruta().id();
     * var rutaBd = rutaServicio.obtenerPorId(rutaId);
     * tramoEntidad.setRuta(rutaBd);
     * Tramo tramoCreado = servicio.crear(tramoEntidad);
     * 
     * return
     * ResponseEntity.status(201).body(DtoHandler.convertirTramoDto(tramoCreado));
     * }
     */
    @Operation(summary = "Actualiza un Tramo", description = "Actualiza un Tramo dado segun id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TramoDtoIn tramoDto) {
        // Soporta la actualizacion parcial, y hay que ver reglas en el servicio
        Tramo tramoActualizado;
        try {
            tramoActualizado = servicio.actualizar(id, tramoDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (ConflictException e) {
            return ResponseEntity.status(409).body(new ErrorRequest(409, e.getMessage()));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAs(ErrorRequest.class));
        }

        Long idRuta = tramoActualizado.getRuta() != null ? tramoActualizado.getRuta().getId() : null;
        Long idOrigen = tramoActualizado.getOrigen() != null ? tramoActualizado.getOrigen().getId() : null;
        Long idDestino = tramoActualizado.getDestino() != null ? tramoActualizado.getDestino().getId() : null;

        TramoDtoOut tramoDtoOut = new TramoDtoOut(tramoActualizado.getId(),
                tramoActualizado.getDistancia(),
                tramoActualizado.getFechaHoraInicio(),
                tramoActualizado.getFechaHoraFin(),
                tramoActualizado.getTipo(),
                tramoActualizado.getEstado(),
                tramoActualizado.getCombustibleConsumido(),
                tramoActualizado.getIdCamion(),
                idRuta,
                idOrigen,
                idDestino,
                tramoActualizado.getCostoAproximado(),
                tramoActualizado.getCostoReal(),
                tramoActualizado.getCostoVolumen(),
                tramoActualizado.getCostoKilometro());

        return ResponseEntity.ok(tramoDtoOut);
    }
/*
    @Operation(summary = "Obtener los Tramos de un transportista", description = "Obtiene los Tramos de un transportista dado")
    @GetMapping("/transportista/{transportista}")
    public ResponseEntity<List<TramoDtoOut>> obtener(@PathVariable String transportista) {
        List<Tramo> tramos = servicio.obtenerPorTransportista(transportista);

        return ResponseEntity.ok(tramos.stream().map((r) -> {
            Long idRuta = r.getRuta() != null ? r.getRuta().getId() : null;
            Long idOrigen = r.getOrigen() != null ? r.getOrigen().getId() : null;
            Long idDestino = r.getDestino() != null ? r.getDestino().getId() : null;

            return new TramoDtoOut(r.getId(),
                    r.getDistancia(),
                    r.getFechaHoraInicio(),
                    r.getFechaHoraFin(),
                    r.getTipo(),
                    r.getEstado(),
                    r.getCombustibleConsumido(),
                    r.getIdCamion(),
                    idRuta,
                    idOrigen,
                    idDestino,
                    r.getCostoAproximado(),
                    r.getCostoReal(),
                    r.getCostoVolumen(),
                    r.getCostoKilometro());
        }).collect(Collectors.toList()));
    }
 */
    @Operation(summary = "Obtener un Tramo", description = "Obtiene un Tramo dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Tramo tramo;
        try {
            tramo = servicio.obtenerPorId(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        }

        Long idRuta = tramo.getRuta() != null ? tramo.getRuta().getId() : null;
        Long idOrigen = tramo.getOrigen() != null ? tramo.getOrigen().getId() : null;
        Long idDestino = tramo.getDestino() != null ? tramo.getDestino().getId() : null;

        TramoDtoOut tramoDtoOut = new TramoDtoOut(tramo.getId(),
                tramo.getDistancia(),
                tramo.getFechaHoraInicio(),
                tramo.getFechaHoraFin(),
                tramo.getTipo(),
                tramo.getEstado(),
                tramo.getCombustibleConsumido(),
                tramo.getIdCamion(),
                idRuta,
                idOrigen,
                idDestino,
                tramo.getCostoAproximado(),
                tramo.getCostoReal(),
                tramo.getCostoVolumen(),
                tramo.getCostoKilometro());
        return ResponseEntity.ok(tramoDtoOut);
    }

    // por ejemplo -> GET /api/tramos?idRuta=5
    @Operation(summary = "Obtener todos los Tramos de una Ruta", description = "Obtiene todos los Tramos de una ruta dada")
    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) Long rutaId) {
        List<Tramo> lista;
        if (rutaId != null) {
            try {
                lista = servicio.obtenerPorIdRuta(rutaId);
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
            }

        } else {
            lista = servicio.listarTodos();
        }
        List<TramoDtoOut> dtos = lista.stream().map((r) -> {
            Long idRuta = r.getRuta() != null ? r.getRuta().getId() : null;
            Long idOrigen = r.getOrigen() != null ? r.getOrigen().getId() : null;
            Long idDestino = r.getDestino() != null ? r.getDestino().getId() : null;

            return new TramoDtoOut(r.getId(),
                    r.getDistancia(),
                    r.getFechaHoraInicio(),
                    r.getFechaHoraFin(),
                    r.getTipo(),
                    r.getEstado(),
                    r.getCombustibleConsumido(),
                    r.getIdCamion(),
                    idRuta,
                    idOrigen,
                    idDestino,
                    r.getCostoAproximado(),
                    r.getCostoReal(),
                    r.getCostoVolumen(),
                    r.getCostoKilometro());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Eliminar un Tramo", description = "Elimina un Tramo dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            servicio.eliminar(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }

}
