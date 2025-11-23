package com.pedidos.service.demo.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commonlib.Enums.EstadosTramo;
import com.commonlib.entidades.Tramo;
import com.pedidos.service.demo.dto.CamionDtoHttp;
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
    //     return repositorio.findByCamionNombreTransportista(transportista);
    // }


    @Transactional
    public Tramo actualizar(Long id, TramoDtoIn tramoActualizado) {
        Tramo existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + id));

        // traer camion si se incluye en el dto
        CamionDtoHttp camion = null; 
        if (tramoActualizado.idCamion() != null) {
            camion = camionesClient.get().uri("/" + tramoActualizado.idCamion()).retrieve().toEntity(CamionDtoHttp.class).getBody();
            if (!camion.disponible()) {
                throw new ConflictException("El camion que se quiere asignar esta ocupado en otro tramo");
            }

            if (existente.getEstado().equals(EstadosTramo.INICIADO) || existente.getEstado().equals(EstadosTramo.FINALIZADO)) {
                throw new ConflictException("No se puede asignar un camion a un tramo que esta " + existente.getEstado().toString());

            }
        }

        manejarAsignacionCamion(existente, camion);

        if (tramoActualizado.fechaInicio() != null) {
            if (!existente.getEstado().equals(EstadosTramo.ASIGNADO))
            {
                throw new ConflictException("No se iniciar un tramo que esta " + existente.getEstado().toString());
            }
            existente.setFechaHoraInicio(tramoActualizado.fechaInicio());
            existente.setEstado(EstadosTramo.INICIADO);
        } 

        if (tramoActualizado.fechaFin() != null) {
            if (!existente.getEstado().equals(EstadosTramo.INICIADO))
            {
                throw new ConflictException("No se puede finalizar un tramo que esta " + existente.getEstado().toString());
            }
            existente.setFechaHoraFin(tramoActualizado.fechaFin());
            existente.setEstado(EstadosTramo.FINALIZADO);
        }

        //existente.setEstado(tramoActualizado.estado() != null ? tramoActualizado.estado() : tramoActualizado.getEstado());
        //existente.setFechaHoraFin(tramoActualizado.getFechaHoraFin());
        existente.setCombustibleConsumido(tramoActualizado.combustibleConsumido() != null ? tramoActualizado.combustibleConsumido() : existente.getCombustibleConsumido());

        //existente.setCostoAproximado(tramoActualizado.costoAproximado() != null ? tramoActualizado.costoAproximado() : existente.getCostoAproximado());

        //existente.setCostoReal(tramoActualizado.costoReal() != null ? tramoActualizado.costoReal() : existente.getCostoReal());


        return repositorio.save(existente);
    }

    @Transactional public Tramo iniciarTramo(Long idTramo) {
        Tramo existente = repositorio.findById(idTramo)
            .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + idTramo));

        if (existente.getTramoAnterior() != null) {
            if (!existente.getTramoAnterior().getEstado().equals(EstadosTramo.FINALIZADO)) {
                throw new ConflictException("No se puede iniciar el tramo si su tramo anterior no fue finalizado, tramo anterior: " + existente.getTramoAnterior().getId());
            }
        }

        TramoDtoIn tramoDtoIn = new TramoDtoIn(LocalDateTime.now(), null, null, null, null, null);

        Tramo tramoActualizado = actualizar(idTramo, tramoDtoIn);

        return tramoActualizado;
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

    private void manejarCostoAproximado(Tramo existente) {
        TarifaDtoHttp tarifaDtoHttp; 
        if (existente.getIdCamion() != null) {

        }
    }




    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Tramo no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

}
