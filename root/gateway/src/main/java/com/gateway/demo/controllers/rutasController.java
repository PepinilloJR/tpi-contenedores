package com.gateway.demo.controllers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PutExchange;

import com.commonlib.dto.DtoHandler;
import com.commonlib.dto.RutaDto;
import com.commonlib.dto.SolicitudDto;
import com.commonlib.dto.TramoDto;
import com.commonlib.dto.UbicacionDto;
import com.commonlib.entidades.Solicitud;

@RestController
@RequestMapping("/controlled/rutas")
public class rutasController {


    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient depositosClient;

    @Autowired
    RestClient distanciaClient;

    @Autowired
    RestClient rutasClient;

    @Autowired 
    RestClient tramosClient;

    @PostMapping
    public ResponseEntity<?> agregarRutas(@RequestBody RutaDto rutaDto) {
        SolicitudDto pedidoActual;
        ArrayList<UbicacionDto> depositoActual = new ArrayList<UbicacionDto>();
        try {
            pedidoActual = pedidosClient.get().uri("/" + rutaDto.solicitud().id()).retrieve()
                    .toEntity(SolicitudDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
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
        Double distanciaEstimada = (((ubiObject.getRoutes().get(0).getDuration() / 60) / 60) / 24);
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
                    TramoDto tdto = new TramoDto(null,pedidoActual.origen(), destino, null, rutaDto, "origen-deposito", null, null, null, null, null, l.getDistance(), null);
                    tramos.add(tdto);
                } else {
                    TramoDto tdto = new TramoDto(null,pedidoActual.origen(), pedidoActual.destino(), null, rutaDto, "origen-deposito", null, null, null, null, null, l.getDistance(), null);
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
                    TramoDto tdto = new TramoDto(null,origen, destino, null, null, "deposito-deposito", null, null, null, null, null, l.getDistance(), null);
                    tramos.add(tdto);
                } else {
                    TramoDto tdto = new TramoDto(null,origen, pedidoActual.destino(), null, null, "deposito-destino", null, null, null, null, null, l.getDistance(), null);
                    tramos.add(tdto);
                }
            }

            c += 1;
        }

        Double distanciaTotal = (double)0;
        for (TramoDto t : tramos) {
            if (t.distancia() == null) {
                return ResponseEntity.status(500).body("La distancia llega vacia en el calculo total");
            }
            distanciaTotal += t.distancia();
            
        }



        pedidoActual = pedidosClient.put().uri("/" + pedidoActual.id()).body(pedidoActual).retrieve().toEntity(SolicitudDto.class).getBody();
        
        RutaDto rutaFinal = new RutaDto(null, pedidoActual, tramos.size(), depositoActual.size(), null, null, distanciaTotal);
        rutaFinal = rutasClient.post().body(rutaFinal).retrieve().toEntity(RutaDto.class).getBody();

        for (TramoDto t : tramos) {
            if (t.distancia() == null) {
                return ResponseEntity.status(500).body("La distancia llega antes de crear el dto");
            }
            TramoDto tdto = new TramoDto(null,t.origen(), t.destino(), null, rutaFinal, t.tipo(), null, null, null, null, null, t.distancia(), null);
            if (tdto.distancia() == null) {
                return ResponseEntity.status(500).body("La distancia llega vacia al crear el dto");
            }
            tramosClient.post().body(tdto).exchange((req, res) -> {System.err.println(req);System.err.println(res); return null;});
        }
        // http de ejemplo
        // http://localhost:5000/route/v1/driving/-58.38,-34.60;-58.40,-34.61;-58.43,-34.62;-58.45,-34.63?steps=true&overview=simplified&geometries=geojson
        return ResponseEntity.status(201).body(rutaFinal);
    }

    @PutExchange("/{id}")
    public ResponseEntity<?> FinalizarRuta( @PathVariable Long id) {
        RutaDto ruta;
        SolicitudDto solicitud;
        List<TramoDto> tramos;
        try {
            ruta = rutasClient.get().uri("/" + id).retrieve().toEntity(RutaDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ruta " + id + " no existe.");
        }

        try {
            solicitud = pedidosClient.get().uri("/" + ruta.solicitud().id()).retrieve().toEntity(SolicitudDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ruta no tiene asignada una solicitud.");
        }

        try {
            tramos = tramosClient.get().uri("?idRuta=" + ruta.id()).retrieve().toEntity(new ParameterizedTypeReference<List<TramoDto>>() {}).getBody();
            if (tramos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ruta no tiene tramos.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ruta no tiene tramos." + e.getMessage());
        }

        Double costoTotal = 0.0;
        for (TramoDto t : tramos) {
            if (t.fechaHoraFin() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("los tramos del pedido deben estar todos finalizados.");
            }

            costoTotal += t.costoReal();
        }

        TramoDto tramoInicial = tramos.get(0);
        TramoDto tramoFinal = tramos.get(tramos.size() - 1);

        Duration duracion = Duration.between(tramoInicial.fechaHoraInicio(), tramoFinal.fechaHoraFin());
        long dias = duracion.toDays();
        Solicitud solicitudReal = DtoHandler.convertirSolicitudEntidad(solicitud);

        solicitudReal.setCostoFinal(costoTotal);
        solicitudReal.setTiempoReal(dias);
        solicitudReal.setEstado("finalizado");
        
        try {
            solicitud = DtoHandler.convertirSolicitudDto(solicitudReal);
            solicitud = pedidosClient.put().uri("/" + solicitud.id()).body(solicitud).retrieve().toEntity(SolicitudDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(500).body("La solicitud fallo al intentar modificarse." + e.getMessage());
        }
        
        return ResponseEntity.ok(solicitud);
    }


    @GetMapping("/{solicitudId}")
    public ResponseEntity<?> rutasTentativas(@PathVariable Long solicitudId, @RequestBody RutaDto rutaDto) {

    SolicitudDto pedidoActual;
    try {
        pedidoActual = pedidosClient.get()
                .uri("/" + solicitudId)
                .retrieve()
                .toEntity(SolicitudDto.class)
                .getBody();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    ArrayList<UbicacionDto> depositoActual = new ArrayList<>();
    for (Long dId : rutaDto.depositosID()) {
        try {
            depositoActual.add(
                depositosClient.get()
                        .uri("/" + dId)
                        .retrieve()
                        .toEntity(UbicacionDto.class)
                        .getBody()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El depósito/ubicación " + dId + " no existe.");
        }
    }

    String ubicaciones = pedidoActual.origen().longitud() + "," + pedidoActual.origen().latitud() + ";";
    for (UbicacionDto u : depositoActual) {
        ubicaciones += u.longitud() + "," + u.latitud() + ";";
    }
    ubicaciones += pedidoActual.destino().longitud() + "," + pedidoActual.destino().latitud();

    RutaResponse response;
    try {
        response = distanciaClient.get()
                .uri("driving/" + ubicaciones +
                     "?steps=true&overview=simplified&geometries=geojson&alternatives=3")
                .retrieve()
                .body(RutaResponse.class);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no hay alternativas? " + "/driving/" + ubicaciones +
                     "?steps=true&overview=simplified&geometries=geojson&alternatives=3");
    }

    if (response == null || response.getRoutes() == null) {
        return ResponseEntity.status(500).body("OSRM no devolvió rutas.");
    }

    List<Object> alternativas = new ArrayList<>();

    int alternativaNum = 1;
    for (Route r : response.getRoutes()) {

        List<Leg> legs = r.getLegs();
        List<TramoDto> tramos = new ArrayList<>();

        int c = 0;
        for (Leg l : legs) {

            if (c == 0) {
                UbicacionDto destino = null;
                if (!depositoActual.isEmpty()) {
                    try { destino = depositoActual.get(0); } catch (Exception ignore) {}
                }

                UbicacionDto dest = destino != null ? destino : pedidoActual.destino();

                TramoDto td = new TramoDto(null,
                        pedidoActual.origen(),
                        dest,
                        null, null, "origen-deposito",
                        null, null, null, null, null,
                        l.getDistance(), null);

                tramos.add(td);

            } else {
                UbicacionDto origen = null;
                UbicacionDto destino = null;

                try { origen = depositoActual.get(c - 1); } catch (Exception ignore) {}
                try { destino = depositoActual.get(c); } catch (Exception ignore) {}

                UbicacionDto d = destino != null ? destino : pedidoActual.destino();

                TramoDto td = new TramoDto(null,
                        origen,
                        d,
                        null, null,
                        destino != null ? "deposito-deposito" : "deposito-destino",
                        null, null, null, null, null,
                        l.getDistance(), null);

                tramos.add(td);
            }

            c++;
        }

        double distanciaTotal = 0;
        for (TramoDto t : tramos) distanciaTotal += t.distancia();

        Map<String, Object> alt = new HashMap<>();
        alt.put("rutaNumero", alternativaNum++);
        alt.put("distanciaTotal", distanciaTotal);
        alt.put("tramos", tramos);

        alternativas.add(alt);
    }

    return ResponseEntity.ok(alternativas);
}
}
