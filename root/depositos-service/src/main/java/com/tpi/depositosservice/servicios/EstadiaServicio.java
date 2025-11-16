package com.tpi.depositosservice.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service; // Necesario

import com.commonlib.entidades.Deposito;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.repositorios.DepositoRepository;
import com.tpi.depositosservice.repositorios.EstadiaRepository;

@Service
public class EstadiaServicio {

    private final EstadiaRepository estadiaRepository;
    private final DepositoRepository depositoRepository; // Para buscar el Depósito por ID

    public EstadiaServicio(EstadiaRepository estadiaRepository, DepositoRepository depositoRepository) {
        this.estadiaRepository = estadiaRepository;
        this.depositoRepository = depositoRepository;
    }

    // Método auxiliar para obtener el Deposito o lanzar excepción
    private Deposito obtenerDeposito(Long idDeposito) {
        return depositoRepository.findById(idDeposito)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el depósito con id: " + idDeposito));
    }
    
    public Estadia crear(Estadia estadia, Long idDeposito) {
        // Asignamos el Deposito real antes de guardar
        estadia.setDeposito(obtenerDeposito(idDeposito));
        return estadiaRepository.save(estadia);
    }

    public Estadia actualizar(Long id, Estadia estadia, Long idDeposito) {
        if (!estadiaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la estadía con id: " + id);
        }
        
        // Asignamos el Deposito y el ID a la entidad
        estadia.setDeposito(obtenerDeposito(idDeposito));
        estadia.setIdEstadia(id); 
        
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