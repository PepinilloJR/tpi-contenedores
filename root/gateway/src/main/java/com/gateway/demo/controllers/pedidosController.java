package com.gateway.demo.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
/* 
@RestController
@RequestMapping("/protected/pedidos")
public class pedidosController {
    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient clientesClient;

    @Autowired
    RestClient contenedoresClient;


    @PostMapping
    public ResponseEntity<SolicitudDto> crear(@RequestBody SolicitudDto solicitudDto) {
        // Entonces al crear una solicitud o hacer un pedido, necesito de datos previos:
        // El cliente y a donde debe llegar el pedido
        try {
            // Chekear que venga el cliente en la request
            if (solicitudDto == null || solicitudDto.clienteDto() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // ===================================
            // Req funcional min 1.2)

            ClienteDto cliente = solicitudDto.clienteDto();
            ClienteDto clienteRegistrado;
            // Registrar el cliente o recuperarlo
            try {
                var clienteRecuperado = clientesClient.get()
                        .uri("/" + cliente.id())
                        .retrieve()
                        .toEntity(ClienteDto.class);
                clienteRegistrado = clienteRecuperado.getBody();

            } catch (RestClientException ex) {
                // SI NO ENCUENTRA EL CLIENTE TIRA 404, HAY QUE CREARLO
                var clienteCreado = clientesClient.post()
                        .uri("")
                        .body(cliente)
                        .retrieve()
                        .toEntity(ClienteDto.class);

                clienteRegistrado = clienteCreado.getBody();
            }

            if (clienteRegistrado == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // ==========================================
            // Req funcional min 1.1)

            // CREAR CONTENEDOR

            ContenedorDto contenedorRequerido;

            // CUIDADO CON ESTO EL ESTADO!!!!??!?!?!
            // Deberia llegar en la request los datos para el contenedor

            if (solicitudDto.contenedorDto() != null) {
                contenedorRequerido = new ContenedorDto(
                        null,
                        solicitudDto.contenedorDto().peso(),
                        solicitudDto.contenedorDto().volumen(),
                        solicitudDto.contenedorDto().estado(), // Deberia ser null siempre si no despues cambiarlo o
                                                               // setearlo segun corresponda
                        solicitudDto.contenedorDto().costoVolumen());
            }

            // Necesario ???
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            var contenedorCreado = contenedoresClient.post()
                    .uri("")
                    .body(contenedorRequerido)
                    .retrieve()
                    .toEntity(ContenedorDto.class);

            if (!contenedorCreado.getStatusCode().is2xxSuccessful() || contenedorCreado.getBody() == null) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            ContenedorDto contenedor = contenedorCreado.getBody();

            // Creacion de la solicitud vamos a setear en borrador siempre por las dudas
            // checkear si hay regla de negocio

            Map<String, Object> solicitudPayload = new HashMap<>();
            solicitudPayload.put("estado", "BORRADOR");
            solicitudPayload.put("costoEstimado", solicitudDto.costoEstimado()); // DEBERIAN SER NULOS ??
            solicitudPayload.put("tiempoEstimado", solicitudDto.tiempoEstimado()); // DEBERIAN SER NULOS ??
            solicitudPayload.put("tiempoReal", solicitudDto.tiempoReal()); // DEBERIAN SER NULOS ??
            solicitudPayload.put("costoFinal", solicitudDto.costoFinal()); // DEBERIAN SER NULOS ??

            Map<String, Object> clienteMap = new HashMap<>();
            clienteMap.put("id", clienteRegistrado.id());
            solicitudPayload.put("cliente", clienteMap);

            Map<String, Object> contenedorMap = new HashMap<>();
            contenedorMap.put("id", contenedor.id());
            solicitudPayload.put("contenedor", contenedorMap);

            var solicitudCreada = pedidosClient.post()
                    .uri("")
                    .body(solicitudPayload)
                    .retrieve()
                    .toEntity(SolicitudDto.class);

            if (!solicitudCreada.getStatusCode().is2xxSuccessful() || solicitudCreada.getBody() == null) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            return ResponseEntity.ok(solicitudCreada.getBody());

        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
*/