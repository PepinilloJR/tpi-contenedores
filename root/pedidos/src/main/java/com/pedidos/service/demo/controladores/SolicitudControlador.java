package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.SolicitudDto;
import com.pedidos.service.demo.servicios.ClienteServicio;
import com.pedidos.service.demo.servicios.ContenedorServicio;
import com.pedidos.service.demo.servicios.SolicitudServicio;

import java.util.stream.Collectors;
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

    public SolicitudControlador(SolicitudServicio servicioSolicitud, ClienteServicio servicioCliente,
            ContenedorServicio servicioContenedor) {
        this.servicioSolicitud = servicioSolicitud;
        this.servicioCliente = servicioCliente;
        this.servicioContenedor = servicioContenedor;
    }

    @PostMapping
    public ResponseEntity<SolicitudDto> crear(@RequestBody SolicitudDto solicitudDto) {

        // Voy a tener q usar el validate y guarda si viene null la id
        Solicitud solicitudEntidad = DtoHandler.convertirSolicitudEntidad(solicitudDto);

        if (solicitudEntidad.getCliente() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (solicitudEntidad.getContenedor() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Crear el cliente si no esta registrado (viene en la solicitud)
        var cliente = servicioCliente.crearSiNoExiste(solicitudEntidad.getCliente());
        // Crear el contenedor que viene en la solicitud
        var contenedor = servicioContenedor.crear(solicitudEntidad.getContenedor());
        // Crear la solicitud
        // Seteamos el estado en "borrador"
        solicitudEntidad.setEstado("borrador");
        solicitudEntidad.setCliente(cliente);
        solicitudEntidad.setContenedor(contenedor);
        Solicitud solicitudCreada = servicioSolicitud.crear(solicitudEntidad);
        return ResponseEntity.status(201).body(DtoHandler.convertirSolicitudDto(solicitudCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDto> actualizar(@PathVariable Long id, @RequestBody SolicitudDto solicitudDto) {
        // costoFinal? costoEstima?
        // tiempoReal? tiempoEstimado
        // Estado

        Solicitud solicitudActual = servicioSolicitud.obtenerPorId(id);

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

        Solicitud SolicitudActualizada = servicioSolicitud.actualizar(id, solicitudActual);

        return ResponseEntity.ok(DtoHandler.convertirSolicitudDto(SolicitudActualizada));

    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto> obtener(@PathVariable Long id) {
        Solicitud solicitud = servicioSolicitud.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirSolicitudDto(solicitud));
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
