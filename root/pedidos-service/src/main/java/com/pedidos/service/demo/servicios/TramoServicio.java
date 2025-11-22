package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commonlib.Enums.EstadosTramo;
import com.commonlib.entidades.Tramo;
import com.pedidos.service.demo.dto.CamionDtoHttp;
import com.pedidos.service.demo.dto.TramoDtoIn;
import com.pedidos.service.demo.exepciones.ConflictException;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.TramoRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TramoServicio {
    @Autowired
    RestClient camionesClient;
    private final TramoRepositorio repositorio;

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

    @Transactional(readOnly = true)
    public List<Tramo> obtenerPorTransportista(String transportista) {
        return repositorio.findByCamionNombreTransportista(transportista);
    }

    // Recordar el transportista solo puede modificar el estado y maybe la fecha
    // hora fin?

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

        // esta logica no resuelve la disponibilidad del camion, y nunca lo hara

        if (camion != null) {
            existente.setIdCamion(camion.id());
            
        } else if (existente.getEstado().equals(EstadosTramo.FINALIZADO)) {
            existente.setIdCamion(null);
        }

        if (tramoActualizado.fechaInicio() != null && existente.getEstado().equals(EstadosTramo.ASIGNADO)) {
            existente.setFechaHoraInicio(tramoActualizado.fechaInicio());
            existente.setEstado(EstadosTramo.INICIADO);
        } else {
            throw new ConflictException("No se iniciar un tramo que esta " + existente.getEstado().toString());
        }

        if (tramoActualizado.fechaFin() != null && existente.getEstado().equals(EstadosTramo.INICIADO)) {
            existente.setFechaHoraFin(tramoActualizado.fechaFin());
            existente.setEstado(EstadosTramo.FINALIZADO);
        } else {
            throw new ConflictException("No se finalizar un tramo que esta " + existente.getEstado().toString());
        }

        //existente.setEstado(tramoActualizado.estado() != null ? tramoActualizado.estado() : tramoActualizado.getEstado());
        //existente.setFechaHoraFin(tramoActualizado.getFechaHoraFin());
        existente.setCombustibleConsumido(tramoActualizado.combustibleConsumido() != null ? tramoActualizado.combustibleConsumido() : existente.getCombustibleConsumido());

        existente.setCostoAproximado(tramoActualizado.costoAproximado() != null ? tramoActualizado.costoAproximado() : existente.getCostoAproximado());

        existente.setCostoReal(tramoActualizado.costoReal() != null ? tramoActualizado.costoReal() : existente.getCostoReal());


        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Tramo no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

}
