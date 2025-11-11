package com.camiones.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camiones.service.demo.entidades.Camion;
import com.camiones.service.demo.exepciones.ResourceNotFoundException;
import com.camiones.service.demo.repositorios.CamionRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CamionServicio {

    private final CamionRepositorio repositorio;

    /* ----------------- CREATE ----------------- */
    @Transactional
    public Camion crear(Camion camion) {
        validarDatos(camion);

        // patente única
        if (camion.getPatente() != null && repositorio.existsByPatente(camion.getPatente())) {
            throw new IllegalArgumentException("Ya existe un camión con patente " + camion.getPatente());
        }

        // disponible por defecto = true
        if (camion.getDisponible() == null) camion.setDisponible(true);

        return repositorio.save(camion);
    }

    /* ----------------- READ ----------------- */
    @Transactional(readOnly = true)
    public List<Camion> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Camion obtenerPorId(Long id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id " + id));
    }

    @Transactional(readOnly = true)
    public List<Camion> obtenerPorPatente(String patente) {
        return repositorio.findByPatente(patente);
    }

    /* ----------------- UPDATE ----------------- */
    @Transactional
    public Camion actualizar(Long id, Camion datos) {
        Camion existente = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id " + id));

        validarDatos(datos);

        // si cambia patente, validar unicidad
        if (datos.getPatente() != null && !datos.getPatente().equals(existente.getPatente())
                && repositorio.existsByPatente(datos.getPatente())) {
            throw new IllegalArgumentException("Ya existe un camión con patente " + datos.getPatente());
        }

        existente.setPatente(datos.getPatente());
        existente.setNombreTransportista(datos.getNombreTransportista());
        existente.setTelefono(datos.getTelefono());
        existente.setCapacidadPesoKg(datos.getCapacidadPesoKg());
        existente.setCapacidadVolumenM3(datos.getCapacidadVolumenM3());
        existente.setCostoPorKm(datos.getCostoPorKm());
        existente.setConsumoCombustibleLx100km(datos.getConsumoCombustibleLx100km());
        existente.setDisponible(datos.getDisponible() != null ? datos.getDisponible() : existente.getDisponible());

        return repositorio.save(existente);
    }

    /* ----------------- DELETE ----------------- */
    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Camión no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

    /* ----------------- reglas de negocio básicas ----------------- */
    private void validarDatos(Camion c) {
        if (c.getPatente() == null || c.getPatente().isBlank()) {
            throw new IllegalArgumentException("La patente es obligatoria");
        }
        if (neg(c.getCapacidadPesoKg()) || neg(c.getCapacidadVolumenM3())
                || neg(c.getCostoPorKm()) || neg(c.getConsumoCombustibleLx100km())) {
            throw new IllegalArgumentException("Capacidades, costo y consumo no pueden ser negativos");
        }
    }

    private boolean neg(Double v) { return v != null && v < 0; }
}
