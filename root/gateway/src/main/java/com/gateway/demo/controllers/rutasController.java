package com.gateway.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.commonlib.dto.RutaDto;
import com.commonlib.dto.SolicitudDto;
import com.commonlib.dto.TramoDto;
import com.commonlib.dto.UbicacionDto;

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
        ArrayList<UbicacionDto> depositoActual = new ArrayList<UbicacionDto>();
        try {
            pedidoActual = pedidosClient.get().uri("/" + rutaDto.solicitudDto().id()).retrieve()
                    .toEntity(SolicitudDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("el pedido ingresado no existe");
        }

        String ubicaciones = pedidoActual.origen().longitud() + "," + pedidoActual.origen().latitud() + ";";
        for (Long dId : rutaDto.depositosID()) {
            try {
                depositoActual.add(depositosClient.get()
                        .uri("/" + dId)
                        .retrieve()
                        .toEntity(UbicacionDto.class)
                        .getBody());
                ubicaciones += depositoActual.get(depositoActual.size() - 1).longitud() + "," + depositoActual.get(depositoActual.size() - 1).latitud() + ";";
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El depósito " + dId + " no existe.");
            }

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
        List<Leg> legs = ubiObject.getRoutes().get(0).getLegs();
        List<TramoDto> tramos = new ArrayList<TramoDto>();
        for (Leg l : legs) {
            if (c == 0) {
                // definir destino
                UbicacionDto destino;
                try {
                    destino = depositoActual.get(c);
                } catch (Exception e) {
                    destino = null;
                }

                if (destino != null) {
                    TramoDto tdto = new TramoDto(null,pedidoActual.origen(), destino, null, rutaDto, "origen-deposito", null, null, null, null, null, l.getDistance());
                    tramos.add(tdto);
                } else {
                    TramoDto tdto = new TramoDto(null,pedidoActual.origen(), pedidoActual.destino(), null, rutaDto, "origen-deposito", null, null, null, null, null, l.getDistance());
                    tramos.add(tdto);
                }

            } else {
                UbicacionDto origen;
                try {
                    origen = depositoActual.get(c - 1);
                } catch (Exception e) {
                    origen = null;
                }

                UbicacionDto destino;
                try {
                    destino = depositoActual.get(c);
                } catch (Exception e) {
                    destino = null;
                }

                if (destino != null) {
                    TramoDto tdto = new TramoDto(null,origen, destino, null, rutaDto, "deposito-deposito", null, null, null, null, null, l.getDistance());
                    tramos.add(tdto);
                } else {
                    TramoDto tdto = new TramoDto(null,origen, pedidoActual.destino(), null, rutaDto, "deposito-destino", null, null, null, null, null, l.getDistance());
                    tramos.add(tdto);
                }
            }

            c += 1;
        }

        Double distanciaTotal = (double)0;
        for (TramoDto t : tramos) {
            distanciaTotal += t.distancia();
        }

        RutaDto rutaFinal = new RutaDto(null, pedidoActual, tramos.size(), depositoActual.size(), null, null, distanciaTotal);

        // http de ejemplo
        // http://localhost:5000/route/v1/driving/-58.38,-34.60;-58.40,-34.61;-58.43,-34.62;-58.45,-34.63?steps=true&overview=simplified&geometries=geojson
        return ResponseEntity.status(201).body(rutaFinal);
    }
}
