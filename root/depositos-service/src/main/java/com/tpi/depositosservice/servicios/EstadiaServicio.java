package com.tpi.depositosservice.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.dto.ContenedorDtoOut;
import com.tpi.depositosservice.dto.EstadiaDtoIn;
import com.tpi.depositosservice.dto.UbicacionDtoOut;
import com.tpi.depositosservice.repositorios.EstadiaRepository;
import com.tpi.depositosservice.restcliente.SolicitudesClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EstadiaServicio {

    private final EstadiaRepository estadiaRepository;
    private final SolicitudesClient solicitudesClient;

    @Transactional
    public Estadia crear(EstadiaDtoIn datos) {
        // Validaciones iniciales
        if (datos == null) {
            throw new IllegalArgumentException("Los datos de la estadía no pueden ser nulos");
        }

        if (datos.idContenedor() == null) {
            throw new IllegalArgumentException("El ID del contenedor no puede ser nulo");
        }

        // 1) Obtengo la solicitud
        var solicitud = solicitudesClient.obtenerSolicitudPorContenedor(datos.idContenedor());
        // 2) Obtener la ruta 
        var ruta = solicitudesClient.obtenerRutaPorSolicitud(solicitud.idContedor());
        // 3) Obtener los tramos
        var tramos = solicitudesClient.obtenerTramosPorRuta(ruta.idRuta());
        // 4) Recorrer los tramos 




        // Obtener y validar el contenedor
        // Si no existe, solicitudesClient ya lanza ResourceNotFoundException
        /* 
        if (estadiaRepository.existsByIdContenedorAndFechaFinIsNull(datos.idContenedor())) {
            throw new IllegalArgumentException(
                    "El contenedor con id " + datos.idContenedor()) +
                            " ya se encuentra en una estadía activa");
        }*/

        // Crear la estadía


        return estadiaRepository.save(estadia);
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