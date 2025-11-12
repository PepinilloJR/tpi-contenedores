package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.RutaDto;
import com.commonlib.TramoDto;
import com.commonlib.UbicacionDto;
import com.pedidos.service.demo.entidades.Ruta;
import com.pedidos.service.demo.entidades.Tramo;
import com.pedidos.service.demo.entidades.Ubicacion;
import com.pedidos.service.demo.servicios.TramoServicio;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/tramos")
public class TramoControlador {
    private final TramoServicio servicio;

    public TramoControlador(TramoServicio servicio) {
        this.servicio = servicio;
    }

    private UbicacionDto convertirUbicacionDto(Ubicacion u) {
        if (u == null)
            return null;
        return new UbicacionDto(u.getId(), u.getNombre(), u.getLatitud(), u.getLongitud());
    }

    private RutaDto convertirRutaDto(Ruta r) {
        if (r == null)
            return null;
        return new RutaDto(r.getId(), r.getCantidadTramos(), r.getCantidadDepositos(), r.getCostoPorTramo());
    }

    private Ubicacion convertirUbicacionEntidad(UbicacionDto dto) {
        if (dto == null)
            return null;
        Ubicacion u = new Ubicacion();
        u.setId(dto.id());
        u.setNombre(dto.nombre());
        u.setLatitud(dto.latitud());
        u.setLongitud(dto.longitud());
        return u;
    }

    private Ruta convertirRutaEntidad(RutaDto dto) {
        if (dto == null)
            return null;
        Ruta r = new Ruta();
        r.setId(dto.id());
        r.setCantidadTramos(dto.cantidadTramos());
        r.setCantidadDepositos(dto.cantidadDepositos());
        r.setCostoPorTramo(dto.costoPorTramo());
        return r;
    }

    private TramoDto convertirTramoDto(Tramo t) {
        if (t == null)
            return null;
        return new TramoDto(
                t.getId(),
                convertirUbicacionDto(t.getOrigen()),
                convertirUbicacionDto(t.getDestino()),
                convertirRutaDto(t.getRuta()),
                t.getTipo(),
                t.getEstado(),
                t.getCostoAproximado(),
                t.getCostoReal(),
                t.getFechaHoraInicio(),
                t.getFechaHoraFin());
    }

    private Tramo convertirTramoEntidad(TramoDto dto) {
        if (dto == null)
            return null;

        Tramo t = new Tramo();
        t.setId(dto.id());
        t.setOrigen(convertirUbicacionEntidad(dto.origen()));
        t.setDestino(convertirUbicacionEntidad(dto.destino()));
        t.setRuta(convertirRutaEntidad(dto.ruta()));
        t.setTipo(dto.tipo());
        t.setEstado(dto.estado());
        t.setCostoAproximado(dto.costoAproximado());
        t.setCostoReal(dto.costoReal());
        t.setFechaHoraInicio(dto.fechaHoraInicio());
        t.setFechaHoraFin(dto.fechaHoraFin());

        return t;
    }

    // Maybe validate
    @PostMapping
    public ResponseEntity<TramoDto> crear(@RequestBody TramoDto tramoDto) {
        Tramo tramoEntidad = convertirTramoEntidad(tramoDto);
        Tramo tramoCreado = servicio.crear(tramoEntidad);
        return ResponseEntity.status(201).body(convertirTramoDto(tramoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TramoDto> actualizar(@PathVariable Long id, @RequestBody TramoDto tramoDto) {
        // Soporta la actualizacion parcial, y hay que ver reglas en el servicio
        Tramo tramoActual = servicio.obtenerPorId(id);
        tramoActual.setEstado(tramoDto.estado() != null ? tramoDto.estado() : tramoActual.getEstado());
        tramoActual.setFechaHoraFin(tramoDto.fechaHoraFin() != null ? tramoDto.fechaHoraFin() : tramoActual.getFechaHoraFin());

        Tramo tramoActualizado = servicio.actualizar(id, tramoActual);

        return ResponseEntity.ok(convertirTramoDto(tramoActualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TramoDto> obtener(@PathVariable Long id) {
        Tramo tramo = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirTramoDto(tramo));
    }

    // por ejemplo -> GET /api/tramos?idRuta=5

    @GetMapping
    public ResponseEntity<List<TramoDto>> obtenerTodos(@RequestParam(required = false) Long rutaId) {
        List<Tramo> lista;
        if (rutaId != null) {
            lista = servicio.obtenerPorIdRuta(rutaId);
        } else {
            lista = servicio.listarTodos();
        }
        List<TramoDto> dtos = lista.stream().map(this::convertirTramoDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
