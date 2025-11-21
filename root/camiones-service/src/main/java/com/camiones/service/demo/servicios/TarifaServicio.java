package com.camiones.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camiones.service.demo.exepciones.ResourceNotFoundException;
import com.camiones.service.demo.repositorios.TarifaRepositorio;
import com.commonlib.entidades.Tarifa;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarifaServicio {

    private final TarifaRepositorio repositorio;

    /* ----------------- CREATE ----------------- */
    @Transactional
    public Tarifa crear(Tarifa tarifa) {
        if (tarifa == null) {
            throw new IllegalArgumentException("Tarifa inválida, no puede ser nula");
        }
        validarDatos(tarifa);
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

    /* ----------------- UPDATE ----------------- */
    @Transactional
    public Tarifa actualizar(Long id, Tarifa datos) {
        Tarifa existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada con id " + id));

        if (datos == null) {
            throw new IllegalArgumentException("Datos de tarifa inválidos");
        }

        // asumimos que el controlador hizo el merge y 'datos' representa el estado
        // deseado
        validarDatos(datos);

        existente.setCostoKilometro(datos.getCostoKilometro());
        existente.setCostoVolumen(datos.getCostoVolumen());

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

        if (neg(t.getCostoKilometro()) || neg(t.getCostoVolumen())) {
            throw new IllegalArgumentException("Los valores de precio no pueden ser negativos");
        }

    }

    private boolean neg(Double v) {
        return v != null && v < 0;
    }
}
