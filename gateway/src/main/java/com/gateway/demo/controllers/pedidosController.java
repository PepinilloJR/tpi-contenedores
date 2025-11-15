package com.gateway.demo.controllers;

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

@RestController
@RequestMapping("/protected/pedidos")
public class pedidosController {
    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient clientesClient;

    @Autowired
    RestClient contenedoresClient;

    @Autowired
    RestClient camionesClient;

    /**
     * Flujo:
     * 1) Validar request (debe traer clienteDto)
     * 2) Obtener o crear cliente en el servicio de clientes
     * 3) Crear el contenedor en el servicio de contenedores (identificación única
     * generada allí)
     * 4) Obtener UN camión disponible (/camiones/disponible)
     * 5) Crear la solicitud en el servicio pedidos incluyendo clienteId,
     * contenedorId, camionId y estado
     * 6) Intentar marcar el camión como no disponible / asignado (si tu servicio lo
     * soporta) -> si falla, borrar la solicitud creada (compensación)
     */

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
            // ===================================
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
            // Obtener CAMION DISPONIBLES

            var camionDisponible = camionesClient.get()
                    .uri("/disponible")
                    .retrieve()
                    .toEntity(CamionDto.class);
            if (!camionDisponible.getStatusCode().is2xxSuccessful() || camionDisponible.getBody() == null) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            CamionDto camion = camionDisponible.getBody();

            // Marcar el camion como no disponible luego

            // ==========================================
            // ==========================================
            // ==========================================
            // Req funcional min 1.1)

            // CREAR CONTENEDOR

            ContenedorDto contenedorRequerido;

            // CUIDADO CON ESTO EL ESTADO!!!!??!?!?!

            if (solicitudDto.contenedorDto() != null) {
                contenedorRequerido = new ContenedorDto(
                        null,
                        solicitudDto.contenedorDto().peso(),
                        solicitudDto.contenedorDto().volumen(),
                        solicitudDto.contenedorDto().estado(),
                        solicitudDto.contenedorDto().costoVolumen());
            }

            // Necesario ???
            else {
                contenedorRequerido = new ContenedorDto();
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

            // Setear la fecha/origen/destino ???

        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
