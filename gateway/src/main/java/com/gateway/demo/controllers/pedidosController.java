package com.gateway.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;

import com.commonlib.dto.*;
import com.commonlib.entidades.*;

@RestController
@RequestMapping("/protected/pedidos")
public class pedidosController {
    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient camionesClient;

    @PostMapping
    public ResponseEntity<SolicitudDto> crear(@RequestBody SolicitudDto solicitudDto) {
        try {
            // Obtener CAMIONES DISPONIBLES
            var camionDisponible = camionesClient.get()
                    .uri("/disponible")
                    .retrieve()
                    .toEntity(CamionDto.class);
            if (!camionDisponible.getStatusCode().is2xxSuccessful() || camionDisponible.getBody() == null) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            CamionDto camion = camionDisponible.getBody();

            // Setear la fecha/origen/destino

        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
