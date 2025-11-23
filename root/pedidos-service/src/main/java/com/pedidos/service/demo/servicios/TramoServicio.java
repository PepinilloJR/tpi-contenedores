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

        if (tramoActualizado == null) {
            throw new IllegalArgumentException("Datos de actualización inválidos");
        }

        Long idCamionActual = existente.getIdCamion();
        Long nuevoIdCamion = tramoActualizado.idCamion();

        // Si intenta CAMBIAR el camión (id distinto) y el tramo está INICIADO o FINALIZADO
        if (!Objects.equals(idCamionActual, nuevoIdCamion)
                && (existente.getEstado().equals(EstadosTramo.INICIADO)
                        || existente.getEstado().equals(EstadosTramo.FINALIZADO))) {
            throw new ConflictException(
                    "No se puede asignar o cambiar un camión a un tramo que está " + existente.getEstado());
        }

        // Manejar asignación / reasignación / liberación de camión
        if (nuevoIdCamion != null || idCamionActual != null) {
            manejarAsignacionCamion(existente, nuevoIdCamion);
        }

        // --- Estados y fechas ---

        // Iniciar tramo: solo si viene fechaInicio en el DTO
        if (tramoActualizado.fechaInicio() != null) {
            if (existente.getEstado().equals(EstadosTramo.ASIGNADO)) {
                existente.setFechaHoraInicio(tramoActualizado.fechaInicio());
                existente.setEstado(EstadosTramo.INICIADO);
            } else {
                throw new ConflictException(
                        "Solo se puede iniciar un tramo que está ASIGNADO. Estado actual: " + existente.getEstado());
            }
        }

        // Finalizar tramo: solo si viene fechaFin en el DTO
        if (tramoActualizado.fechaFin() != null) {
            if (existente.getEstado().equals(EstadosTramo.INICIADO)) {
                existente.setFechaHoraFin(tramoActualizado.fechaFin());
                existente.setEstado(EstadosTramo.FINALIZADO);
            } else {
                throw new ConflictException(
                        "Solo se puede finalizar un tramo que está INICIADO. Estado actual: " + existente.getEstado());
            }
        }

        // Campos opcionales de costos y combustible
        if (tramoActualizado.combustibleConsumido() != null) {
            existente.setCombustibleConsumido(tramoActualizado.combustibleConsumido());
        }

        if (tramoActualizado.costoAproximado() != null) {
            existente.setCostoAproximado(tramoActualizado.costoAproximado());
        }

        if (tramoActualizado.costoReal() != null) {
            existente.setCostoReal(tramoActualizado.costoReal());
        }

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
