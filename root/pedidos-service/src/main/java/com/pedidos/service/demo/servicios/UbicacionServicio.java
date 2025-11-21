package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Ubicacion;
import com.pedidos.service.demo.dto.UbicacionDtoIn;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.UbicacionRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionServicio {
    private final UbicacionRepositorio repositorio;

    @Transactional
    public Ubicacion crear(UbicacionDtoIn ubicacionDto) {
        TiposUbicacion tipo;

        try {
            tipo = TiposUbicacion.valueOf(ubicacionDto.tipo().toUpperCase());
        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(new ErrorRequest(400, "El tipo de ubicacion es Null o invalido"));
            throw new IllegalArgumentException("Error al crear, el tipo de ubicacion es Null o invalido");
        }
        
        Ubicacion ubicacionEntidad = new Ubicacion(null, ubicacionDto.nombre(), tipo, ubicacionDto.latitud(), ubicacionDto.longitud(), ubicacionDto.costo());

        if (ubicacionEntidad.getCostoEstadia() == null && ubicacionEntidad.getTipo().equals(TiposUbicacion.DEPOSITO)) {
            //return ResponseEntity.badRequest().body("Error al crear, un deposito requiere un costo de estadia");
            throw new IllegalArgumentException("Error al crear, un deposito requiere un costo de estadia");

        }
        
        return repositorio.save(ubicacionEntidad);
    }

    @Transactional
    public Ubicacion crearSiNoExiste(Ubicacion ubicacion) {
        return repositorio.findByLatitudEqualsAndLongitudEquals(ubicacion.getLatitud(), ubicacion.getLongitud())
                .orElseGet(() -> repositorio.save(ubicacion));
    }

    @Transactional(readOnly = true)
    public List<Ubicacion> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Ubicacion obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicacion no encontrada con id " + id));
    }

    @Transactional
    public Ubicacion actualizar(Long id, UbicacionDtoIn ubicacionDto) {

        Ubicacion existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicacion no encontrada con id " + id));


        existente.setLatitud(ubicacionDto.latitud() != null ? ubicacionDto.latitud() : existente.getLatitud());
        existente.setLongitud(ubicacionDto.longitud() != null ? ubicacionDto.longitud() : existente.getLongitud());
        existente.setNombre(ubicacionDto.nombre() != null ? ubicacionDto.nombre() : existente.getNombre());
        existente.setCostoEstadia(ubicacionDto.costo() != null ? ubicacionDto.costo() : existente.getCostoEstadia());

        if (existente.getCostoEstadia() == null && existente.getTipo().equals(TiposUbicacion.DEPOSITO)) {
            throw new IllegalArgumentException ("Error al actualizar, un deposito requiere un costo de estadia");
        }

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Ubicacion no encontrada con id " + id);
        }
        repositorio.deleteById(id);
    }

}
