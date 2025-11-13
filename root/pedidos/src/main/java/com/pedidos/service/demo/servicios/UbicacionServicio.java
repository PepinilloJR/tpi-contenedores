package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pedidos.service.demo.entidades.Ubicacion;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.UbicacionRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionServicio {
    private final UbicacionRepositorio repositorio;

    @Transactional
    public Ubicacion crear(Ubicacion ubicacion) {
        return repositorio.save(ubicacion);
    }

    @Transactional(readOnly = true)
    public List<Ubicacion> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Ubicacion obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicacion no encontrada con id " + id));
    }

    @Transactional
    public Ubicacion actualizar(Long id, Ubicacion ubicacionActualizada) {
        Ubicacion existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicacion no encontrada con id " + id));

        existente.setLatitud(ubicacionActualizada.getLatitud());
        existente.setLongitud(ubicacionActualizada.getLongitud());
        existente.setNombre(ubicacionActualizada.getNombre());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Ubicacion no encontrada con id " + id);
        }
        repositorio.deleteById(id);
    }

}
