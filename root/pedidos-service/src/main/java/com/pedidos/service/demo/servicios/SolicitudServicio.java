package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.entidades.Solicitud;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.SolicitudRepositorio;

import lombok.RequiredArgsConstructor;

// Calcular costo y el tiempo estimado inicial
// crear la solicitud y asociar al contenedor y al cliente
// setear el estado inicial en pendiente

// ruta tentativa?

@Service
@RequiredArgsConstructor
public class SolicitudServicio {
    private final SolicitudRepositorio repositorio;

    @Transactional
    public Solicitud crear(Solicitud solicitud) {
        return repositorio.save(solicitud);
    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Solicitud obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id " + id));
    }

    @Transactional(readOnly = true)
    public List<Solicitud> obtenerPorClienteId(Long id) {
        return repositorio.findByClienteId(id);
    }

    @Transactional
    public Solicitud actualizar(Long id, Solicitud solicitudActualizada) {
        Solicitud existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id " + id));

        existente.setCliente(solicitudActualizada.getCliente());
        existente.setContenedor(solicitudActualizada.getContenedor());
        existente.setCostoEstimado(solicitudActualizada.getCostoEstimado());
        existente.setCostoFinal(solicitudActualizada.getCostoFinal());
        existente.setTiempoEstimado(solicitudActualizada.getTiempoEstimado());
        existente.setTiempoReal(solicitudActualizada.getTiempoReal());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Solicitud no encontradacon id" + id);
        }
        repositorio.deleteById(id);
    }

}
