package com.gateway.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.commonlib.dto.DepositoDto;
import com.commonlib.dto.RutaDto;
import com.commonlib.dto.SolicitudDto;

@RestController
@RequestMapping("/controlled/rutas")
public class rutasController {


    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient depositosClient;

    @Autowired
    RestClient distanciaClient;

    @PostMapping
    public ResponseEntity<?> agregarRutas(@RequestBody RutaDto rutaDto) {
        SolicitudDto pedidoActual;
        DepositoDto depositoActual;
        try {
            pedidoActual = pedidosClient.get().uri("/" + rutaDto.solicitudDto().id()).retrieve()
                    .toEntity(SolicitudDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("el pedido ingresado no existe");
        }

        String ubicaciones = pedidoActual.origen().longitud() + "," + pedidoActual.origen().latitud() + ";";
        for (Long dId : rutaDto.depositosID()) {
            try {
                depositoActual = depositosClient.get()
                        .uri("/" + dId)
                        .retrieve()
                        .toEntity(DepositoDto.class)
                        .getBody();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El depósito " + dId + " no existe.");
            }

            ubicaciones += depositoActual.longitud() + "," + depositoActual.latitud() + ";";
        }
        ubicaciones += pedidoActual.destino().longitud() + "," + pedidoActual.destino().latitud();
        // /route/v1/driving/"+ubicaciones
        RutaResponse ubiObject;
        try {
            ubiObject = distanciaClient.get()
                    .uri("/driving/" + ubicaciones + "?steps=true&overview=simplified&geometries=geojson").retrieve().body(RutaResponse.class);
            System.out.println(ubiObject.toString());

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getCause());
        }
        
        int c = 0;
        for (Leg l : ubiObject.getRoutes().get(0).getLegs()) {
            if (c == 0) {

            }
        }

        // http de ejemplo
        // http://localhost:5000/route/v1/driving/-58.38,-34.60;-58.40,-34.61;-58.43,-34.62;-58.45,-34.63?steps=true&overview=simplified&geometries=geojson
        return ResponseEntity.status(201).body(ubiObject.getRoutes());
    }
}
