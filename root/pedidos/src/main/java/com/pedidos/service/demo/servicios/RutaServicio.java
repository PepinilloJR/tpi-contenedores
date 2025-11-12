package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedidos.service.demo.repositorios.RutaRepositorio;
import com.pedidos.service.demo.entidades.Ruta;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RutaServicio {
    private final RutaRepositorio repositorio;

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

    // Fijarse bien esto

    @Transactional
    public Ruta actualizar(Long id, Ruta rutaActualizada) {
        Ruta existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id " + id));

        existente.setCantidadDepositos(rutaActualizada.getCantidadDepositos());
        existente.setCantidadTramos(rutaActualizada.getCantidadTramos());
        existente.setCostoPorTramo(rutaActualizada.getCostoPorTramo());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Ruta no encontrada con id " + id);
        }
        repositorio.deleteById(id);
    }
}
