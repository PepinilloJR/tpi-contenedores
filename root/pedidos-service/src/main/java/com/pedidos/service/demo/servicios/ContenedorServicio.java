package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.EstadosContenedor;
import com.commonlib.entidades.Contenedor;
import com.commonlib.entidades.Ubicacion;
import com.pedidos.service.demo.dto.ContenedorDtoIn;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.ContenedorRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContenedorServicio {

    private final ContenedorRepositorio repositorio;
    private final UbicacionServicio ubicacionServicio;
    //private final SolicitudServicio solicitudServicio;


    // NO CONTROLA QUE PESO, VOLUMEN SEAN MAYOR A 0

    @Transactional
    public Contenedor crear(ContenedorDtoIn contenedorDto) {

        Ubicacion ubicacion = null;

        if (contenedorDto.idUbicacionUltima() != null) {
            ubicacion = ubicacionServicio.obtenerPorId(contenedorDto.idUbicacionUltima());
        }

        Contenedor contenedor = new Contenedor(
                null,
                contenedorDto.peso(),
                contenedorDto.volumen(),
                contenedorDto.estado(),
                ubicacion);
        return repositorio.save(contenedor);
    }

    @Transactional(readOnly = true)
    public List<Contenedor> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Contenedor obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contenedor no encontrado con id " + id));
    }


    @Transactional(readOnly = true)
    public Contenedor obtenerPorEstado(EstadosContenedor estado) {
        return repositorio.findByEstadoOrderByIdAsc(estado)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontraron contenedores en estado " + estado));
    }

    @Transactional(readOnly = true)
    public List<Contenedor> listarPendientes() {
        return repositorio.findByEstado(EstadosContenedor.EN_PREPARACION);
    }

    @Transactional
    public Contenedor actualizar(Long id, ContenedorDtoIn contenedorDto) {
        Contenedor existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));

        Ubicacion ubicacion = null;

        if (contenedorDto.idUbicacionUltima() != null) {
            ubicacion = ubicacionServicio.obtenerPorId(contenedorDto.idUbicacionUltima());
        }

        existente.setEstado(contenedorDto.estado() != null ? contenedorDto.estado() : existente.getEstado());
        existente.setPeso(contenedorDto.peso() != null ? contenedorDto.peso() : existente.getPeso());
        existente.setVolumen(contenedorDto.volumen() != null ? contenedorDto.volumen() : existente.getVolumen());
        existente.setUbicacion(ubicacion != null ? ubicacion : existente.getUbicacion());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id" + id);
        }
        repositorio.deleteById(id);
    }

}
