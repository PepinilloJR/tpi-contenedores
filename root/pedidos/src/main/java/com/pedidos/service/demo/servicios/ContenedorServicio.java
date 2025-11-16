package com.pedidos.service.demo.servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.commonlib.entidades.Contenedor;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.ContenedorRepositorio;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenedorServicio {
    private final ContenedorRepositorio repositorio;

    @Transactional
    public Contenedor crear(Contenedor contenedor) {
        return repositorio.save(contenedor);
    }

    @Transactional(readOnly = true)
    public List<Contenedor> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Contenedor obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }

    @Transactional(readOnly = true)
    public Contenedor obtenerPorEstado(String estado) {
        return repositorio.findByEstadoOrderByIdAsc(estado)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontraron contenedores en estado " + estado));
    }

    @Transactional(readOnly = true)
    public List<Contenedor> listarPendientes() {
        return repositorio.findByEstado("pendiente");
    }

    @Transactional
    public Contenedor actualizar(Long id, Contenedor contenedorActualizado) {
        Contenedor existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));

        existente.setEstado(contenedorActualizado.getEstado());
        existente.setPeso(contenedorActualizado.getPeso());
        existente.setVolumen(contenedorActualizado.getVolumen());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id" + id);
        }
        repositorio.deleteById(id);
    }

}
