package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.UbicacionDto;
import com.pedidos.service.demo.entidades.Ubicacion;
import com.pedidos.service.demo.servicios.UbicacionServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionControlador {
    private final UbicacionServicio servicio;

    public UbicacionControlador(UbicacionServicio servicio) {
        this.servicio = servicio;
    }

    private UbicacionDto convertirUbicacionDto(Ubicacion u) {
        if (u == null)
            return null;
        return new UbicacionDto(u.getId(), u.getNombre(), u.getLatitud(), u.getLongitud());
    }

    private Ubicacion convertirUbicacionEntidad(UbicacionDto dto) {
        Ubicacion u = new Ubicacion();
        u.setNombre(dto.nombre());
        u.setLongitud(dto.longitud());
        u.setLatitud(dto.latitud());
        return u;
    }

    @PostMapping
    public ResponseEntity<UbicacionDto> crear(@RequestBody UbicacionDto ubicacionDto) {
        Ubicacion ubicacionEntidad = convertirUbicacionEntidad(ubicacionDto);
        Ubicacion ubicacionCreada = servicio.crear(ubicacionEntidad);

        return ResponseEntity.status(201).body(convertirUbicacionDto(ubicacionCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDto> actualizar(@PathVariable Long id, @RequestBody UbicacionDto ubicacionDto) {

        Ubicacion ubicacionActual = servicio.obtenerPorId(id);

        ubicacionActual.setNombre(ubicacionDto.nombre() != null ? ubicacionDto.nombre() : ubicacionActual.getNombre());
        ubicacionActual.setLatitud(ubicacionDto.latitud() != null ? ubicacionDto.latitud() : ubicacionActual.getLatitud());
        ubicacionActual.setLongitud(ubicacionDto.longitud() != null ? ubicacionDto.longitud() : ubicacionActual.getLongitud());

        Ubicacion ubicacionActualizada = servicio.actualizar(id, ubicacionActual);

        return ResponseEntity.ok(convertirUbicacionDto(ubicacionActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDto> obtener(@RequestParam Long id) {
        Ubicacion ubicacion = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirUbicacionDto(ubicacion));
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDto>> getMethodName() {
        List<Ubicacion> lista = servicio.listarTodos();
        List<UbicacionDto> dtos = lista.stream().map(this::convertirUbicacionDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
