package com.pedidos.service.demo.servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.entidades.Seguimiento;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.SeguimientoRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeguimientoServicio {
    private final SeguimientoRepositorio repositorio;

    @Transactional
    public Seguimiento crear(Seguimiento seguimiento) {
        return repositorio.save(seguimiento);
    }

    @Transactional(readOnly = true)
    public Seguimiento obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado con id " + id));
    }

    @Transactional
    public Seguimiento actualizarSeguimiento(Long id, Seguimiento seguimientoActualizado) {
        Seguimiento existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado con id " + id));
        existente.setEstado(seguimientoActualizado.getEstado());
        existente.setFecha(seguimientoActualizado.getFecha());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Seguimiento no encontrado con id" + id);
        }
        repositorio.deleteById(id);
    }

}
