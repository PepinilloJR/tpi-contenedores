package com.pedidos.service.demo.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonlib.ClienteDto;
import com.commonlib.RutaDto;
import com.commonlib.TramoDto;
import com.commonlib.UbicacionDto;
import com.pedidos.service.demo.entidades.Cliente;
import com.pedidos.service.demo.entidades.Ruta;
import com.pedidos.service.demo.entidades.Tramo;
import com.pedidos.service.demo.entidades.Ubicacion;
import com.pedidos.service.demo.servicios.TramoServicio;

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

}
