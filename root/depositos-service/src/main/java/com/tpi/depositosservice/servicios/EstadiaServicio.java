package com.tpi.depositosservice.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service; // Necesario

import com.commonlib.entidades.Estadia;
import com.commonlib.entidades.Tramo;
import com.tpi.depositosservice.repositorios.UbicacionRepository;
import com.tpi.depositosservice.repositorios.EstadiaRepository;
import com.tpi.depositosservice.repositorios.TramoRepositorio;

@Service
public class EstadiaServicio {

    private final EstadiaRepository estadiaRepository;
    private final UbicacionRepository depositoRepository; // Para buscar el Depósito por ID
    private final TramoRepositorio tramoRepositorio;
    public EstadiaServicio(EstadiaRepository estadiaRepository, UbicacionRepository depositoRepository, TramoRepositorio tramoRepositorio) {
        this.estadiaRepository = estadiaRepository;
        this.depositoRepository = depositoRepository;
        this.tramoRepositorio = tramoRepositorio;
    }

    public Estadia crear(Estadia estadia) {
        // Asignamos el Deposito real antes de guardar
        
        return estadiaRepository.save(estadia);
    }

    public Tramo obtener(Long id) {
        return tramoRepositorio.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró el tramo con id: " + id));
    }

    public Estadia actualizar(Estadia estadia) {
        if (!estadiaRepository.existsById(estadia.getIdEstadia())) {
            throw new NoSuchElementException("No se encontró la estadía con id: " + estadia.getIdEstadia());
        }

        // Asignamos el Deposito y el ID a la entidad;
        
        return estadiaRepository.save(estadia);
    }

    public Estadia obtenerPorId(Long id) {
        return estadiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la estadía con id: " + id));
    }

    public List<Estadia> listarTodos() {
        return estadiaRepository.findAll();
    }

    public void eliminar(Long id) {
        if (!estadiaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la estadía con id: " + id);
        }
        estadiaRepository.deleteById(id);
    }
}