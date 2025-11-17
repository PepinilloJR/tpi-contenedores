package com.gateway.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.commonlib.dto.CamionDto;
import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.EstadiaDto;
import com.commonlib.dto.TramoDto;
import com.commonlib.entidades.Estadia;
import com.commonlib.entidades.Tramo;

@RestController
@RequestMapping("/controlled/tramos")
public class tramosController {
    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient contenedoresClient;

    @Autowired
    RestClient rutasClient;

    @Autowired
    RestClient tramosClient;

    @Autowired
    RestClient camionesClient;

    @Autowired
    RestClient estadiasClient;

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarTramo(@PathVariable Long id,
            @RequestParam(required = false) LocalDateTime fechaHoraEntrada,
            @RequestParam(required = false) LocalDateTime fechaHoraSalida, @RequestBody TramoDto tramoDto) {
        Tramo tramoActual;
        try {
            TramoDto tramoActualDto = tramosClient.get().uri("/" + id).retrieve().toEntity(TramoDto.class).getBody();
            tramoActual = DtoHandler.convertirTramoEntidad(tramoActualDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tramo " + id + " no existe.");
        }

        if (tramoActual.getCamion() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El tramo debe tener un camion asignado para ser finalizado" + tramoActual);
        }

        if (tramoActual.getFechaHoraInicio() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El tramo debe haber sido iniciado, no exite fecha de inicio" + tramoActual);
        }

        if (tramoDto.fechaHoraInicio() != null) {
            tramoActual.setFechaHoraFin(tramoDto.fechaHoraFin());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe indicarse una fecha de finalizacion");
        }

        if (tramoDto.combustibleConsumido() != null) {
            tramoActual.setCombustibleConsumido(tramoDto.combustibleConsumido());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe indicarse una fecha de finalizacion");

        }

        tramoDto = DtoHandler.convertirTramoDto(tramoActual);
        EstadiaDto estadiaDto = null;
        if (fechaHoraEntrada != null && fechaHoraSalida != null) {
            estadiaDto = new EstadiaDto(null, tramoDto, fechaHoraEntrada, fechaHoraSalida);
            if (tramoDto.origen().costo() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("el origen del tramo no tiene un costo de estadia");

            }
            try {
                estadiaDto = estadiasClient.post().body(estadiaDto).retrieve().toEntity(EstadiaDto.class).getBody();
            } catch (Exception e) {
                return ResponseEntity.status(500)
                        .body("Error creando estadia: " + e.getMessage());

            }
        }
        tramoActual.setEstado("finalizado");

        if (estadiaDto != null) {
            Estadia estadia = DtoHandler.convertirEstadiaEntidad(estadiaDto);
            tramoActual.calcularCostoReal(estadia.calcularCostoEstadia());
        } else {
            tramoActual.calcularCostoReal(null);
        }
        // calculo del costo real del tramo


        TramoDto tramoActualDto = DtoHandler.convertirTramoDto(tramoActual);
        try {
            tramoActualDto = tramosClient.put().uri("/" + id).body(tramoActualDto).retrieve().toEntity(TramoDto.class)
                    .getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error modificando el tramo: " + e.getMessage());
        }





        return ResponseEntity.ok(tramoActualDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> asignarCamion(@PathVariable Long id, @RequestBody TramoDto tramoDto) {
        // Obtener el tramo
        TramoDto tramoActual;
        CamionDto camionApto;

        try {
            if (tramoDto == null) {
                return ResponseEntity.badRequest().body(null);
            }

            tramoActual = tramosClient.get()
                    .uri("/" + id)
                    .retrieve()
                    .toEntity(TramoDto.class)
                    .getBody();

            if (tramoActual.ruta() == null || tramoActual.ruta().solicitud() == null
                    || tramoActual.ruta().solicitud().contenedor() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El tramo no tiene ruta, solicitud o contenedor asignado");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("el tramo ingresado no existe");
        }

        // hay que comprobar que el camion sea apto
        // primero tratar de obtener la ruta del tramo

        var volumen = tramoActual.ruta().solicitud().contenedor().volumen();
        var peso = tramoActual.ruta().solicitud().contenedor().peso();

        if (peso == null || volumen == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Peso o volumen del contenedor no definidos");
        }

        // Validar que el camión asignado sea apto para el tramo

        try {
            camionApto = camionesClient.get()
                    .uri("/disponible/por-capacidad?peso=" + peso + "&volumen=" + volumen)
                    .retrieve()
                    .toEntity(CamionDto.class)
                    .getBody();

            if (camionApto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No hay camión disponible para la capacidad requerida");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al validar camión disponible: " + e.getMessage());
        }

        CamionDto camionActualizado;
        try {
            camionActualizado = new CamionDto(camionApto.id(), camionApto.tarifa(), camionApto.patente(),
                    camionApto.nombreTransportista(), camionApto.telefonoTransportista(), camionApto.capacidadPeso(),
                    camionApto.capacidadVolumen(), camionApto.consumoCombustiblePromedio(), camionApto.disponible());

            camionesClient.put()
                    .uri("/" + camionApto.id())
                    .body(camionActualizado)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado del camión: " + e.getMessage());
        }



        TramoDto tramoConCamion = new TramoDto(
                tramoActual.id(),
                tramoActual.origen(),
                tramoActual.destino(),
                camionActualizado,
                tramoActual.ruta(),
                tramoActual.tipo(),
                "pendiente",
                tramoActual.costoAproximado(),
                tramoActual.costoReal(),
                tramoActual.fechaHoraInicio(),
                tramoActual.fechaHoraFin(),
                tramoActual.distancia(),
                tramoActual.combustibleConsumido());

        var tramo = DtoHandler.convertirTramoEntidad(tramoConCamion);
        tramo.calcularCostoAproximado();

        var tramoDtoFinal = DtoHandler.convertirTramoDto(tramo);
        

        // Actualizar el estado del camion
        tramoConCamion = tramosClient.put().uri("/" + id).body(tramoDtoFinal).retrieve().toEntity(TramoDto.class)
                .getBody();

        return ResponseEntity.ok(tramoConCamion);

    }
}
