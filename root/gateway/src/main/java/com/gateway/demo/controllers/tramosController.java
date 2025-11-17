package com.gateway.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.commonlib.dto.CamionDto;
import com.commonlib.dto.TramoDto;

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
                tramoActual.distancia());

        // Actualizar el estado del camion
        tramoConCamion = tramosClient.put().uri("/" + id).body(tramoConCamion).retrieve().toEntity(TramoDto.class).getBody();

        return ResponseEntity.ok(tramoConCamion);

    }

}
