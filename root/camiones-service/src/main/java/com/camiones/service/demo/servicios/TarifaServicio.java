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

        // en tu controlador ya hicimos el merge de campos no nulos sobre "datos",
        // así que acá asumimos que "datos" viene completo y coherente

        validarDatos(datos);


        existente.setCostoKilometro(datos.getCostoKilometro());
        existente.setCostoVolumen(datos.getCostoVolumen());
        existente.setCostoLitro(datos.getCostoLitro());

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

        if (neg(t.getCostoKilometro()) || neg(t.getCostoVolumen()) || neg(t.getCostoLitro())) {
            throw new IllegalArgumentException("Los valores de precio no pueden ser negativos");
        }

    }

    private boolean neg(Double v) {
        return v != null && v < 0;
    }
}
