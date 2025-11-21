package com.tpi.depositosservice.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Ubicacion;
import com.tpi.depositosservice.repositorios.UbicacionRepository;

@Service
public class UbicacionServicio {

    private final UbicacionRepository repository;

    public UbicacionServicio(UbicacionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Ubicacion crear(Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("Ubicacion invalida, no puede ser nula");
        }
        
        validarDatos(ubicacion);

        // Chequear que no exista otra ubicación con la misma latitud y longitud
        var existente = repository.findByLatitudAndLongitud(
                ubicacion.getLatitud(), ubicacion.getLongitud());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una ubicación con la misma latitud y longitud");
        }

        return repository.save(ubicacion);
    }

    @Transactional
    public Ubicacion actualizar(Long id, Ubicacion ubicacion) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la ubicación con id: " + id);
        }

        validarDatos(ubicacion);

        // Chequear que no exista otra ubicación con la misma latitud y longitud
        var existente = repository.findByLatitudAndLongitud(
                ubicacion.getLatitud(), ubicacion.getLongitud());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra ubicación con la misma latitud y longitud");
        }

        ubicacion.setId(id); // aseguramos que el ID sea el correcto
        return repository.save(ubicacion);
    }

    @Transactional(readOnly = true)
    public Ubicacion obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la ubicación con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Ubicacion> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la ubicación con id: " + id);
        }
        repository.deleteById(id);
    }

    private void validarDatos(Ubicacion u) {
        if (u.getNombre() == null || u.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la ubicacion es obligatorio");
        }
        if (u.getLatitud() == null || u.getLatitud() < 0) {
            throw new IllegalArgumentException("La latitud debe ser un valor positivo y no nulo");
        }
        if (u.getLongitud() == null || u.getLongitud() < 0) {
            throw new IllegalArgumentException("La longitud debe ser un valor positivo y no nulo");
        }
        if (u.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de la ubicacion es obligatorio");
        }
        // Validar que tipo sea uno de los valores del enum
        boolean valido = false;
        for (TiposUbicacion t : TiposUbicacion.values()) {
            if (t == u.getTipo()) {
                valido = true;
                break;
            }
        }
        if (!valido) {
            throw new IllegalArgumentException("El tipo de la ubicacion es invalido");
        }

        // Costo de estadía puede ser nulo, pero si no lo es, debe ser >= 0
        if (u.getCostoEstadia() != null && u.getCostoEstadia() < 0) {
            throw new IllegalArgumentException("El costo de estadia no puede ser negativo");
        }

        // Si costoEstadia existe pero tipo no es DEPOSITO, lo anulamos
        if (u.getCostoEstadia() != null && u.getTipo() != TiposUbicacion.DEPOSITO) {
            u.setCostoEstadia(null);
        }
    }

}