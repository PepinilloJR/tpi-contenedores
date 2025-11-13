package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidos.service.demo.servicios.ContenedorServicio;

import jakarta.validation.Valid;

import com.commonlib.dto.ContenedorDto;
import com.commonlib.entidades.Contenedor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/contenedores")
public class ContenedorControlador {
    private final ContenedorServicio servicio;

    public ContenedorControlador(ContenedorServicio servicio) {
        this.servicio = servicio;
    }

    // helper privado entidad -> dto
    private ContenedorDto convertirContenedorDto(Contenedor c) {
        if (c == null)
            return null;
        return new ContenedorDto(c.getId(), c.getPeso(), c.getVolumen(), c.getEstado(), c.getCostoVolumen());
    }

    // helper privado dto -> entidad (para crear)
    private Contenedor convertirContenedorEntidad(ContenedorDto dto) {
        Contenedor c = new Contenedor();
        c.setPeso(dto.peso());
        c.setVolumen(dto.volumen());
        c.setEstado(dto.estado());
        c.setCostoVolumen(dto.costoVolumen());
        return c;
    }

    @PostMapping
    public ResponseEntity<ContenedorDto> crear(@Valid @RequestBody ContenedorDto contenedorDto) {
        Contenedor contenedorEntidad = convertirContenedorEntidad(contenedorDto);
        Contenedor contenedorCreado = servicio.crear(contenedorEntidad);
        return ResponseEntity.status(201).body(convertirContenedorDto(contenedorCreado));
    }

    // En el servicio hay que controlar el costoVolumen

    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDto> putMethodName(@PathVariable Long id, @RequestBody ContenedorDto contenedorDto) {
        Contenedor contenedorActual = servicio.obtenerPorId(id);

        contenedorActual.setPeso(contenedorDto.peso() != null ? contenedorDto.peso() : contenedorActual.getPeso());
        contenedorActual.setVolumen(contenedorDto.volumen() != null ? contenedorDto.volumen() : contenedorActual.getVolumen());
        contenedorActual.setEstado(contenedorDto.estado() != null ? contenedorDto.estado() : contenedorActual.getEstado());
        contenedorActual.setCostoVolumen(contenedorDto.costoVolumen() != null ? contenedorDto.costoVolumen() : contenedorActual.getCostoVolumen());
        
        Contenedor contenedorActualizado = servicio.actualizar(id, contenedorActual);
        return ResponseEntity.ok(convertirContenedorDto(contenedorActualizado));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDto> obtener(@PathVariable Long id) {
        Contenedor contenedor = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirContenedorDto(contenedor));
    }
    

    @GetMapping
    public ResponseEntity<List<ContenedorDto>> obtenerTodos() {
        List<Contenedor> lista = servicio.listarTodos();
        List<ContenedorDto> dtos = lista.stream().map(this::convertirContenedorDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    

}
