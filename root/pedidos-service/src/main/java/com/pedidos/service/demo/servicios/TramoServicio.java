package com.pedidos.service.demo.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commonlib.Enums.EstadoSolicitud;
import com.commonlib.Enums.EstadosContenedor;
import com.commonlib.Enums.EstadosTramo;
import com.commonlib.Enums.TiposTramos;
import com.commonlib.entidades.Tramo;
import com.pedidos.service.demo.dto.CamionDtoHttp;
import com.pedidos.service.demo.dto.ContenedorDtoIn;
import com.pedidos.service.demo.dto.SolicitudDtoIn;
import com.pedidos.service.demo.dto.TarifaDtoHttp;
import com.pedidos.service.demo.dto.TramoDtoIn;
import com.pedidos.service.demo.exepciones.ConflictException;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.TramoRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TramoServicio {

    private final TramoRepositorio repositorio;
    private final SolicitudServicio solicitudServicio;
    private final ContenedorServicio contenedorServicio;

    @Autowired
    private RestClient camionesClient;
    @Autowired
    private RestClient tarifasClient;

    @Transactional
    public Tramo crear(Tramo tramo) {
        return repositorio.save(tramo);
    }

    @Transactional(readOnly = true)
    public List<Tramo> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Tramo obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + id));
    }

    @Transactional(readOnly = true)
    public List<Tramo> obtenerPorIdRuta(Long idR) {
        return repositorio.findByRutaId(idR);
    }

    // @Transactional(readOnly = true)
    // public List<Tramo> obtenerPorTransportista(String transportista) {
    // return repositorio.findByCamionNombreTransportista(transportista);
    // }

    @Transactional
    public Tramo actualizar(Long id, TramoDtoIn tramoActualizado) {
        Tramo existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + id));

        // traer camion si se incluye en el dto

        if (existente.getRuta().getSolicitud() == null) {
            throw new ConflictException(
                    "No se pueden realizar acciones sobre un tramo cuya ruta no esta asignada a ningun pedido");

        }

        CamionDtoHttp camion = null;
        if (tramoActualizado.idCamion() != null) {
            camion = camionesClient.get().uri("/" + tramoActualizado.idCamion()).retrieve()
                    .toEntity(CamionDtoHttp.class).getBody();
            if (!camion.disponible()) {
                throw new ConflictException("El camion que se quiere asignar esta ocupado en otro tramo");
            }

            if (existente.getEstado().equals(EstadosTramo.INICIADO)
                    || existente.getEstado().equals(EstadosTramo.FINALIZADO)) {
                throw new ConflictException(
                        "No se puede asignar un camion a un tramo que esta " + existente.getEstado().toString());

            }
        }

        if (tramoActualizado.fechaFin() != null) {
            if (!existente.getEstado().equals(EstadosTramo.INICIADO)) {
                throw new ConflictException(
                        "No se puede finalizar un tramo que esta " + existente.getEstado().toString());
            }
            if (tramoActualizado.combustibleConsumido() == null) {
                throw new IllegalArgumentException("Se debe ingresar el combustible consumido para finalizar un tramo");
            }
            existente.setFechaHoraFin(tramoActualizado.fechaFin());
            existente.setCombustibleConsumido(tramoActualizado.combustibleConsumido());
            manejarCalculoReal(existente);
            existente.setEstado(EstadosTramo.FINALIZADO);

            // debo manejar los estados del contendor, dar su ubicacion y si esta en un
            // deposito o si ya llego
            EstadosContenedor nuevoEstadoContenedor;
            if (existente.getTipo().equals(TiposTramos.ORIGEN_DESTINO) || existente.getTipo().equals(TiposTramos.DEPOSITO_DESTINO))
            {
                nuevoEstadoContenedor = EstadosContenedor.ENTREGADO;
            } else {
                nuevoEstadoContenedor = EstadosContenedor.EN_DEPOSITO;
            }
            ContenedorDtoIn contenedorDtoIn = new ContenedorDtoIn(null, null, null, nuevoEstadoContenedor,
                    existente.getDestino().getId());
            contenedorServicio.actualizar(existente.getRuta().getSolicitud().getContenedor().getId(), contenedorDtoIn);

        }

        manejarAsignacionCamion(existente, camion);

        if (camion != null) {
            manejarCostoAproximado(existente, camion);
        }

        if (tramoActualizado.fechaInicio() != null) {
            if (!existente.getEstado().equals(EstadosTramo.ASIGNADO)) {
                throw new ConflictException("No se iniciar un tramo que esta " + existente.getEstado().toString());
            }

            existente.setFechaHoraInicio(tramoActualizado.fechaInicio());
            existente.setEstado(EstadosTramo.INICIADO);

            // si es el primer tramo, entonces debo setear que la solicitud esta en viaje

            if (existente.getTramoAnterior() != null) {
            if (!existente.getTramoAnterior().getEstado().equals(EstadosTramo.FINALIZADO)) {
                throw new ConflictException(
                        "No se puede iniciar el tramo si su tramo anterior no fue finalizado, tramo anterior: "
                                + existente.getTramoAnterior().getId());
            }
            }

            if (existente.getTipo().equals(TiposTramos.ORIGEN_DEPOSITO)
                    || existente.getTipo().equals(TiposTramos.ORIGEN_DESTINO)) {
                SolicitudDtoIn solicitudDtoIn = new SolicitudDtoIn(EstadoSolicitud.EN_TRANSITO, null);
                solicitudServicio.actualizar(existente.getRuta().getSolicitud().getId(), solicitudDtoIn);
            }

            // debo manejar los estados del contendor, dar su ubicacion y si esta en un
            // deposito o si ya llego

            ContenedorDtoIn contenedorDtoIn = new ContenedorDtoIn(null, null, null, EstadosContenedor.EN_VIAJE,
                    existente.getOrigen().getId());
            contenedorServicio.actualizar(existente.getRuta().getSolicitud().getContenedor().getId(), contenedorDtoIn);
        }

        return repositorio.save(existente);
    }

    @Transactional
    public Tramo iniciarTramo(Long idTramo) {
        Tramo existente = repositorio.findById(idTramo)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + idTramo));

                /* 
        if (existente.getTramoAnterior() != null) {
            if (!existente.getTramoAnterior().getEstado().equals(EstadosTramo.FINALIZADO)) {
                throw new ConflictException(
                        "No se puede iniciar el tramo si su tramo anterior no fue finalizado, tramo anterior: "
                                + existente.getTramoAnterior().getId());
            }
        }
        */
        // Mandarle al dtoIn el id del camion apto
        Long camionAptoId = null;
        if (existente.getIdCamion() == null) {
            CamionDtoHttp camionApto = camionesClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/disponible/por-capacidad")
                            .queryParam("peso", existente.getRuta().getSolicitud().getContenedor().getPeso())
                            .queryParam("volumen", existente.getRuta().getSolicitud().getContenedor().getVolumen())
                            .build())
                    .retrieve().toEntity(CamionDtoHttp.class).getBody();
            camionAptoId = camionApto.id();
        }

        TramoDtoIn tramoDtoIn = new TramoDtoIn(LocalDateTime.now(), null, null, camionAptoId, null, null);

        Tramo tramoActualizado = actualizar(idTramo, tramoDtoIn);

        return tramoActualizado;
    }

    @Transactional
    public Tramo finalizarTramo(Long idTramo, TramoDtoIn tramoActualizado) {
        // no deberia hacer falta obtener la tarifa ya que los costos quedan guardados
        // al asignarse un camion, y al calcular el costo aproximado
        if (tramoActualizado.combustibleConsumido() == null) {
            throw new IllegalArgumentException("Se debe ingresar el combustible consumido durante el tramo");
        }

        Integer combustible = tramoActualizado.combustibleConsumido();

        TramoDtoIn tramoDtoIn = new TramoDtoIn(null, LocalDateTime.now(), combustible, null, null, null);

        Tramo tramo = actualizar(idTramo, tramoDtoIn);

        return tramo;
    }

    private void manejarCalculoReal(Tramo tramo) {
        //Integer combustible = tramo.getCombustibleConsumido();
        Double volumen = tramo.getRuta().getSolicitud().getContenedor().getVolumen();

        Double distanciaKm = tramo.getDistancia() / 1000;

        Double parteCombustible = distanciaKm * tramo.getCostoKilometro();
        Double parteVolumen = volumen * tramo.getCostoVolumen();

        Double costoReal = parteCombustible + parteVolumen;

        tramo.setCostoReal(costoReal);
    }

    private void manejarAsignacionCamion(Tramo existente, CamionDtoHttp camion) {
        if (camion != null) {
            if (existente.getIdCamion() != null) {
                camionesClient.put()
                        .uri("/{id}/liberar", existente.getIdCamion())
                        .retrieve()
                        .toBodilessEntity();
            }

            existente.setIdCamion(camion.id());

            camionesClient.put()
                    .uri("/{id}/ocupar", existente.getIdCamion())
                    .retrieve()
                    .toBodilessEntity();

            existente.setEstado(EstadosTramo.ASIGNADO);

        } else if (existente.getEstado().equals(EstadosTramo.FINALIZADO)) {

            if (existente.getIdCamion() != null) {
                camionesClient.put()
                        .uri("/{id}/liberar", existente.getIdCamion())
                        .retrieve()
                        .toBodilessEntity();
            }

            existente.setIdCamion(null);
        }

        // una vez asigne un camion, puedo obtener el costo aproximado

    }

    private void manejarCostoAproximado(Tramo existente, CamionDtoHttp camionDtoHttp) {
        TarifaDtoHttp tarifaDtoHttp;

        tarifaDtoHttp = tarifasClient.get().uri("/" + camionDtoHttp.idTarifa()).retrieve().toEntity(TarifaDtoHttp.class)
                .getBody();

        // debo obtener los datos del contenedor del tramo para poder realizar el
        // calculo aproximado
        Double volumen = existente.getRuta().getSolicitud().getContenedor().getVolumen();

        Double parteCombustible = tarifaDtoHttp.costoKilometro() * camionDtoHttp.consumoCombustiblePromedio();
        Double parteContenedor = tarifaDtoHttp.costoVolumen() * volumen;

        existente.setCostoAproximado(parteContenedor + parteCombustible);
        existente.setCostoKilometro(tarifaDtoHttp.costoKilometro());
        existente.setCostoVolumen(tarifaDtoHttp.costoVolumen());
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Tramo no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

}
