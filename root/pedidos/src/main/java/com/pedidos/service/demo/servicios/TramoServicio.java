package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonlib.entidades.Tramo;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.TramoRepositorio;

import jakarta.transaction.TransactionScoped;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TramoServicio {
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
    public Tramo actualizar(Long id, Tramo tramoActualizado) {
        Tramo existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con id " + id));

        existente.setEstado(tramoActualizado.getEstado());
        existente.setFechaHoraFin(tramoActualizado.getFechaHoraFin());

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
