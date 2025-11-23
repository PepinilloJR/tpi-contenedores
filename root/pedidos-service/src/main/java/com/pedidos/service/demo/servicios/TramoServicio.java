package com.pedidos.service.demo.servicios;

import java.util.List;
import java.util.Objects;

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

    private final TramoRepositorio repositorio;

    @Autowired
    private RestClient camionesClient;

    /* ----------------- CREATE ----------------- */

    @Transactional
    public Tramo crear(Tramo tramo) {
        return repositorio.save(tramo);
    }

    /* ----------------- READ ----------------- */

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

    /* ----------------- UPDATE ----------------- */
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

    /* ----------------- DELETE ----------------- */

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Tramo no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

    /* ----------------- HELPER: manejo de camiones ----------------- */

    /**
     * Maneja la lógica de:
     * - liberar el camión actual (si hay y cambia)
     * - validar que el nuevo camión exista y esté disponible
     * - marcar como ocupado el nuevo camión
     * - actualizar el idCamion del tramo
     */
    private void manejarAsignacionCamion(Tramo existente, Long nuevoIdCamion) {
        Long idCamionActual = existente.getIdCamion();

        // 0) Si no cambia el camión, no hacemos nada
        if (Objects.equals(idCamionActual, nuevoIdCamion)) {
            return;
        }

        // 1) Liberar el camión anterior si había uno
        if (idCamionActual != null) {
            camionesClient.put()
                    .uri("/{id}/liberar", idCamionActual)
                    .retrieve()
                    .toBodilessEntity();
        }

        // 2) Si el nuevo id es null, dejamos el tramo sin camión
        if (nuevoIdCamion == null) {
            existente.setIdCamion(null);
            return;
        }

        // 3) Traer el nuevo camión y validar disponibilidad
        CamionDtoHttp camionNuevo = camionesClient.get()
                .uri("/{id}", nuevoIdCamion)
                .retrieve()
                .toEntity(CamionDtoHttp.class)
                .getBody();

        if (camionNuevo == null) {
            throw new ResourceNotFoundException("Camión no encontrado con id " + nuevoIdCamion);
        }

        if (!camionNuevo.disponible()) {
            throw new ConflictException("El camión que se quiere asignar está ocupado en otro tramo");
        }

        // 4) Marcarlo como ocupado en el microservicio de camiones
        camionesClient.put()
                .uri("/{id}/ocupar", nuevoIdCamion)
                .retrieve()
                .toBodilessEntity();

        // 5) Guardar relación en el tramo
        existente.setIdCamion(nuevoIdCamion);
    }

}
