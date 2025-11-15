package com.camiones.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camiones.service.demo.entidades.Tarifa;
import com.camiones.service.demo.exepciones.ResourceNotFoundException;
import com.camiones.service.demo.repositorios.TarifaRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarifaServicio {

    private final TarifaRepositorio repositorio;

    /* ----------------- CREATE ----------------- */
    @Transactional
    public Tarifa crear(Tarifa tarifa) {
        validarDatos(tarifa);

        // activo por defecto = true
        if (tarifa.getActivo() == null) {
            tarifa.setActivo(true);
        }

        return repositorio.save(tarifa);
    }

    /* ----------------- READ ----------------- */
    @Transactional(readOnly = true)
    public List<Tarifa> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Tarifa obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada con id " + id));
    }

    @Transactional(readOnly = true)
    public List<Tarifa> obtenerPorNombre(String nombre) {
        return repositorio.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Tarifa> listarActivas() {
        return repositorio.findByActivoTrue();
    }

    /* ----------------- UPDATE ----------------- */
    @Transactional
    public Tarifa actualizar(Long id, Tarifa datos) {
        Tarifa existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada con id " + id));

        // en tu controlador ya hicimos el merge de campos no nulos sobre "datos",
        // así que acá asumimos que "datos" viene completo y coherente

        validarDatos(datos);

        existente.setNombre(datos.getNombre());
        existente.setPrecioPorKm(datos.getPrecioPorKm());
        existente.setPrecioFijo(datos.getPrecioFijo());
        existente.setMoneda(datos.getMoneda());
        existente.setVigenciaDesde(datos.getVigenciaDesde());
        existente.setVigenciaHasta(datos.getVigenciaHasta());
        existente.setActivo(datos.getActivo() != null ? datos.getActivo() : existente.getActivo());

        return repositorio.save(existente);
    }

    /* ----------------- DELETE ----------------- */
    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Tarifa no encontrada con id " + id);
        }
        repositorio.deleteById(id);
    }

    /* ----------------- reglas de negocio básicas ----------------- */
    private void validarDatos(Tarifa t) {
        if (t.getNombre() == null || t.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarifa es obligatorio");
        }
        if (neg(t.getPrecioPorKm()) || neg(t.getPrecioFijo())) {
            throw new IllegalArgumentException("Los valores de precio no pueden ser negativos");
        }
        if (t.getVigenciaDesde() == null) {
            throw new IllegalArgumentException("La fecha de vigencia desde es obligatoria");
        }
        if (t.getVigenciaHasta() != null && t.getVigenciaHasta().isBefore(t.getVigenciaDesde())) {
            throw new IllegalArgumentException("La fecha de vigencia hasta no puede ser anterior a vigencia desde");
        }
    }

    private boolean neg(Double v) {
        return v != null && v < 0;
    }
}
