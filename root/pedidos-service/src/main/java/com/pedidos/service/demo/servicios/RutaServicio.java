package com.pedidos.service.demo.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commonlib.Enums.EstadoSolicitud;
import com.commonlib.Enums.EstadosTramo;
import com.commonlib.Enums.TiposTramos;
import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Ruta;
import com.commonlib.entidades.Solicitud;
import com.commonlib.entidades.Tramo;
import com.commonlib.entidades.Ubicacion;
import com.pedidos.service.demo.dto.RutaDtoIn;
import com.pedidos.service.demo.dto.RutaTentativaDtoIn;
import com.pedidos.service.demo.dto.RutaTentativaDtoOut;
import com.pedidos.service.demo.dto.SolicitudDtoIn;
import com.pedidos.service.demo.exepciones.ConflictException;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.osrmObjects.Leg;
import com.pedidos.service.demo.osrmObjects.Route;
import com.pedidos.service.demo.osrmObjects.RutaResponse;
import com.pedidos.service.demo.repositorios.RutaRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RutaServicio {
    private final RutaRepositorio repositorio;
    private final SolicitudServicio solicitudServicio;
    private final UbicacionServicio ubicacionServicio;
    private final TramoServicio tramoServicio;

    @Autowired
    RestClient distanciaClient;

    @Transactional
    public Ruta crear(Ruta ruta) {
        return repositorio.save(ruta);
    }

    @Transactional
    public List<Ruta> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Ruta obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id " + id));
    }

    @Transactional(readOnly = true)
    public Ruta obtenerPorIdSolicitud(Long idSolicitud) {
        return repositorio.findBySolicitudId(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con solicitud cuya id es " + idSolicitud));
    }

    // Fijarse bien esto

    @Transactional
    public Ruta actualizar(Long id, RutaDtoIn rutaActualizada) {
        Ruta existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id " + id));

        existente.setCantidadDepositos(rutaActualizada.cantidadDepositos() != null ? rutaActualizada.cantidadDepositos()
                : existente.getCantidadDepositos());

        existente.setCantidadTramos(rutaActualizada.cantidadTramos() != null ? rutaActualizada.cantidadTramos()
                : existente.getCantidadTramos());
        existente.setCostoPorTramo(rutaActualizada.costoPorTramo() != null ? rutaActualizada.costoPorTramo()
                : existente.getCostoPorTramo());
        existente.setTiempoReal(
                rutaActualizada.tiempoReal() != null ? rutaActualizada.tiempoReal() : existente.getTiempoReal());

        if (rutaActualizada.solicitudId() != null) {
            Solicitud solicitud = solicitudServicio.obtenerPorId(rutaActualizada.solicitudId());
            if (!Objects.equals(solicitud.getId(), existente.getIdSolicitudBorrador())) {
                throw new ConflictException(
                        "La ruta fue creada para la solicitud: " + existente.getIdSolicitudBorrador());
            }
            existente.setSolicitud(solicitud);
        }

        return repositorio.save(existente);
    }

    // necesito que funcione bien solicitud todavia
    @Transactional
    public Ruta asignarRutaSolicitud(Long idRuta, Long idSolicitud) {
        Ruta existente = repositorio.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id " + idRuta));

        return repositorio.save(existente);
    }

    @Transactional
    public List<RutaTentativaDtoOut> crearRutasTentativas(RutaTentativaDtoIn dto) {
        Solicitud solicitud;
        List<Ubicacion> depositos = new ArrayList<>();
        List<RutaTentativaDtoOut> rutas = new ArrayList<>();
        String waypointString;

        solicitud = solicitudServicio.obtenerPorId(dto.pedidoId());

        // se va creando la url mientras voy cargando los depositos

        waypointString = solicitud.getOrigen().getLongitud() + "," + solicitud.getOrigen().getLatitud();

        for (Long depositosId : dto.depositosId()) {
            Ubicacion deposito = ubicacionServicio.obtenerPorId(depositosId);

            if (deposito.getTipo() != TiposUbicacion.DEPOSITO) {
                throw new IllegalArgumentException("La ubicacion " + depositosId + " no es tipo DEPOSITO");
            }

            depositos.add(deposito);
            waypointString = waypointString + ";" + deposito.getLongitud() + "," + deposito.getLatitud();
        }

        waypointString = waypointString + ";" + solicitud.getDestino().getLongitud() + ","
                + solicitud.getDestino().getLatitud();

        // obtengo las routes

        RutaResponse osrmResponse = distanciaClient.get()
                .uri("driving/" + waypointString +
                        "?steps=true&overview=simplified&geometries=geojson&alternatives=3")
                .retrieve()
                .body(RutaResponse.class);

        // voy creando las rutas y sus tramos con distancia y tiempo estimado

        for (Route r : osrmResponse.getRoutes()) {

            Ruta ruta = Ruta.builder()
                    .distanciaTotal(r.getDistance())
                    .tiempoEstimado(r.getDuration())
                    .cantidadTramos(r.getLegs().size()) // cada leg equivale a un tramo
                    .cantidadDepositos(depositos.size())
                    .idSolicitudBorrador(solicitud.getId())
                    .build();
            ruta = crear(ruta);

            // creo los tramos de la ruta

            // si no hay depositos, es un tramo origen-destino
            if (depositos.isEmpty()) {
                Tramo tramo = Tramo.builder()
                        .origen(solicitud.getOrigen())
                        .destino(solicitud.getDestino())
                        .distancia(ruta.getDistanciaTotal())
                        .tipo(TiposTramos.ORIGEN_DESTINO)
                        .estado(EstadosTramo.PENDIENTE)
                        .ruta(ruta)
                        .build();

                tramoServicio.crear(tramo);

            } else {

                int c = 0;
                for (Leg l : r.getLegs()) {
                    Tramo tramo = new Tramo();
                    // si recien empieza, significa que el origen es el del pedido
                    tramo.setDistancia(l.getDistance());
                    tramo.setRuta(ruta);
                    if (c == 0) {
                        tramo.setOrigen(solicitud.getOrigen());
                        tramo.setDestino(depositos.get(c));
                        tramo.setTipo(TiposTramos.ORIGEN_DEPOSITO);
                    }

                    else if (c < depositos.size()) {
                        tramo.setOrigen(depositos.get(c - 1));
                        tramo.setDestino(depositos.get(c));
                        tramo.setTipo(TiposTramos.DEPOSITO_DEPOSITO);
                    } else {
                        tramo.setOrigen(depositos.get(c - 1));
                        tramo.setDestino(solicitud.getDestino());
                        tramo.setTipo(TiposTramos.DEPOSITO_DESTINO);

                    }
                    tramo.setEstado(EstadosTramo.PENDIENTE);
                    c++;

                    tramoServicio.crear(tramo);

                }
            }

            rutas.add(new RutaTentativaDtoOut(ruta.getId(), ruta.getDistanciaTotal(), ruta.getTiempoEstimado(),
                    ruta.getCantidadTramos(), ruta.getCantidadDepositos()));

        }

        return rutas;
    }

    @Transactional
    public Ruta asignarSolicitud(Long idRuta, Long idSolicitud) {
        // Ruta ruta = rutaServicio.obtenerPorId(idRuta);
        // Solicitud solicitud = obtenerPorId(idRuta); -> no hace falta porque el
        // servicio de actualizar ruta ya comprueba todo esto antes

        RutaDtoIn rutaCambio = new RutaDtoIn(null, null, null, null, idSolicitud);
        Ruta rutaNueva = actualizar(idRuta, rutaCambio);

        SolicitudDtoIn solicitudCambio = new SolicitudDtoIn(EstadoSolicitud.PROGRAMADA, null, null);

        solicitudServicio.actualizar(idSolicitud, solicitudCambio);
        return rutaNueva;
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Ruta no encontrada con id " + id);
        }
        repositorio.deleteById(id);
    }
}
