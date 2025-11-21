package com.camiones.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camiones.service.demo.exepciones.ResourceNotFoundException;
import com.camiones.service.demo.repositorios.CamionRepositorio;
import com.commonlib.entidades.Camion;
import com.commonlib.entidades.Tarifa;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CamionServicio {

    private final CamionRepositorio repositorio;

    @Transactional
    public Camion crear(Camion camion) {
        if (camion == null) {
            throw new IllegalArgumentException("Camion invalido, no puede ser nulo");
        }

        validarDatos(camion);

        // patente única
        if (camion.getPatente() != null && repositorio.existsByPatente(camion.getPatente())) {
            throw new IllegalArgumentException("Ya existe un camión con patente " + camion.getPatente());
        }

        // disponible por defecto = true
        if (camion.getDisponible() == null)
            camion.setDisponible(true);

        return repositorio.save(camion);
    }

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

    @Transactional(readOnly = true)
    public Camion obtenerDisponible() {
        return repositorio.findByDisponibleTrueOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("No hay ningun camion disponible"));
    }

    @Transactional(readOnly = true)
    public Camion obtenerDisponiblePorCapacidad(Double peso, Double volumen) {
        return repositorio
                .findFirstByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso,
                        volumen)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay ningun camion disponible o que cumpla con la capacidad de peso de " + peso
                                + " y volumen de " + volumen));
    }

    @Transactional
    public Camion actualizar(Long id, Camion datos) {
        Camion existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id " + id));

        if (datos == null) {
            throw new IllegalArgumentException("Datos de actualización inválidos");
        }

        // si intenta cambiar la patente, validar unicidad (comparando con la existente)
        if (datos.getPatente() != null && !datos.getPatente().equals(existente.getPatente())
                && repositorio.existsByPatente(datos.getPatente())) {
            throw new IllegalArgumentException("Ya existe un camión con patente " + datos.getPatente());
        }

        // Merge: aplicar solo los campos no nulos de 'datos' sobre 'existente'
        if (datos.getPatente() != null)
            existente.setPatente(datos.getPatente());
        if (datos.getNombreTransportista() != null)
            existente.setNombreTransportista(datos.getNombreTransportista());
        if (datos.getTelefonoTransportista() != null)
            existente.setTelefonoTransportista(datos.getTelefonoTransportista());
        if (datos.getCapacidadPeso() != null)
            existente.setCapacidadPeso(datos.getCapacidadPeso());
        if (datos.getCapacidadVolumen() != null)
            existente.setCapacidadVolumen(datos.getCapacidadVolumen());
        if (datos.getConsumoCombustiblePromedio() != null)
            existente.setConsumoCombustiblePromedio(datos.getConsumoCombustiblePromedio());
        if (datos.getDisponible() != null)
            existente.setDisponible(datos.getDisponible());

        // Tarifa: si 'datos' trae una tarifa no nula, la aplicamos; si no, mantenemos
        // la existente
        Tarifa tarifaNueva = datos.getTarifa();
        if (tarifaNueva != null) {
            existente.setTarifa(tarifaNueva);
        }

        // Validar el objeto resultante (coincide con constraints de la entidad)
        validarDatos(existente);

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
        if (neg(c.getCapacidadPeso()) || neg(c.getCapacidadVolumen())
                || neg(c.getConsumoCombustiblePromedio())) {
            throw new IllegalArgumentException("Capacidades y consumo no pueden ser negativos");
        }
    }

    private boolean neg(Double v) {
        return v != null && v < 0;
    }
}
