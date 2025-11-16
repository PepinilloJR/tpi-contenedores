package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.SeguimientoDto;
import com.commonlib.dto.SolicitudDto;
import com.pedidos.service.demo.servicios.ClienteServicio;
import com.pedidos.service.demo.servicios.ContenedorServicio;
import com.pedidos.service.demo.servicios.SolicitudServicio;
import com.pedidos.service.demo.servicios.UbicacionServicio;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commonlib.entidades.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudControlador {
    private final SolicitudServicio servicioSolicitud;
    private final ClienteServicio servicioCliente;
    private final ContenedorServicio servicioContenedor;
    private final UbicacionServicio servicioUbicacion;

    public SolicitudControlador(SolicitudServicio servicioSolicitud, ClienteServicio servicioCliente,
            ContenedorServicio servicioContenedor, UbicacionServicio servicioUbicacion) {
        this.servicioSolicitud = servicioSolicitud;
        this.servicioCliente = servicioCliente;
        this.servicioContenedor = servicioContenedor;
        this.servicioUbicacion = servicioUbicacion;
    }

    @PostMapping
    public ResponseEntity<SolicitudDto> crear(@RequestBody SolicitudDto solicitudDto) {

        System.out.println(solicitudDto);

        // 1) Validaciones previas antes de convertir
        if (solicitudDto == null || solicitudDto.cliente() == null || solicitudDto.contenedor() == null
                || solicitudDto.origen() == null || solicitudDto.destino() == null) {
            return ResponseEntity.badRequest().build();
        }

        // 2) Convertir seguro (ya sabemos que cliente y contenedor no son null)
        Solicitud solicitudEntidad;
        try {
            solicitudEntidad = DtoHandler.convertirSolicitudEntidad(solicitudDto);
        } catch (Exception e) {
            // Si la conversión falla por otro motivo, devolvemos 400 con info mínima
            return ResponseEntity.badRequest().build();
        }

        // 3) Persistir/obtener cliente y contenedor y reasignarlos
        var cliente = servicioCliente.crearSiNoExiste(solicitudEntidad.getCliente());
        System.out.println(cliente);
        var contenedor = servicioContenedor.crear(solicitudEntidad.getContenedor());
        var origen = servicioUbicacion.crearSiNoExiste(solicitudEntidad.getOrigen());
        var destino = servicioUbicacion.crearSiNoExiste(solicitudEntidad.getDestino());

        // 4) Preparar y guardar la solicitud
        solicitudEntidad.setEstado("borrador");
        solicitudEntidad.setCliente(cliente);
        solicitudEntidad.setContenedor(contenedor);
        solicitudEntidad.setOrigen(origen);
        solicitudEntidad.setDestino(destino);

        Solicitud solicitudCreada = servicioSolicitud.crear(solicitudEntidad);

        Seguimiento primerSeguimiento = new Seguimiento();
        primerSeguimiento.setEstado("borrador");
        primerSeguimiento.setFecha(LocalDateTime.now());

        if (solicitudCreada.getSeguimiento() == null) {
            solicitudCreada.setSeguimiento(new ArrayList<>());
        }

        solicitudCreada.getSeguimiento().add(primerSeguimiento);

        servicioSolicitud.actualizar(solicitudCreada.getId(), solicitudCreada);

        // 5) Responder 201 con el DTO resultante
        return ResponseEntity.status(201).body(DtoHandler.convertirSolicitudDto(solicitudCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDto> actualizar(@PathVariable Long id, @RequestBody SolicitudDto solicitudDto) {
        // costoFinal? costoEstima?
        // tiempoReal? tiempoEstimado
        // Estado

        Solicitud solicitudActual = servicioSolicitud.obtenerPorId(id);

        String estadoAnterior = solicitudActual.getEstado();

        solicitudActual.setTiempoEstimado(solicitudDto.tiempoEstimado() != null ? solicitudDto.tiempoEstimado()
                : solicitudActual.getTiempoEstimado());

        solicitudActual.setCostoEstimado(solicitudDto.costoEstimado() != null ? solicitudDto.costoEstimado()
                : solicitudActual.getCostoEstimado());

        solicitudActual.setTiempoReal(solicitudDto.tiempoReal() != null ? solicitudDto.tiempoReal()
                : solicitudActual.getTiempoReal());

        solicitudActual.setCostoFinal(solicitudDto.costoFinal() != null ? solicitudDto.costoFinal()
                : solicitudActual.getCostoFinal());

        solicitudActual.setEstado(solicitudDto.estado() != null ? solicitudDto.estado()
                : solicitudActual.getEstado());

        if (!solicitudActual.getEstado().equals(estadoAnterior)) {
            Seguimiento nuevoSeguimiento = new Seguimiento();
            nuevoSeguimiento.setEstado(solicitudActual.getEstado());
            nuevoSeguimiento.setFecha(LocalDateTime.now());

            if (solicitudActual.getSeguimiento() == null) {
                solicitudActual.setSeguimiento(new ArrayList<>());
            }

            solicitudActual.getSeguimiento().add(nuevoSeguimiento);

        }
        Solicitud SolicitudActualizada = servicioSolicitud.actualizar(id, solicitudActual);

        return ResponseEntity.ok(DtoHandler.convertirSolicitudDto(SolicitudActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto> obtener(@PathVariable Long id) {
        Solicitud solicitud = servicioSolicitud.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirSolicitudDto(solicitud));
    }

    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<List<SeguimientoDto>> obtenerSegumiento(@PathVariable Long id) {
        Solicitud solicitud = servicioSolicitud.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirSeguimientosDto(solicitud.getSeguimiento()));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDto>> obtenerTodos() {
        List<Solicitud> lista = servicioSolicitud.listarTodos();
        List<SolicitudDto> dtos = lista.stream().map(DtoHandler::convertirSolicitudDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioSolicitud.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
