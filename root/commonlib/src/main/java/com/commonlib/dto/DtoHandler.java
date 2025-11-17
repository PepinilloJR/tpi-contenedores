package com.commonlib.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.commonlib.entidades.Camion;
import com.commonlib.entidades.Cliente;
import com.commonlib.entidades.Contenedor;
import com.commonlib.entidades.Ruta;
import com.commonlib.entidades.Seguimiento;
import com.commonlib.entidades.Solicitud;
import com.commonlib.entidades.Tramo;
import com.commonlib.entidades.Ubicacion;

public interface DtoHandler {

    // Seguimiento

    // Para un solo seguimiento
    public static SeguimientoDto convertirSeguimientoDto(Seguimiento s) {
        if (s == null)
            return null;
        return new SeguimientoDto(s.getId(), s.getEstado(), s.getFecha());
    }

    // Para un solo seguimientoDto
    public static Seguimiento convertirSeguimientoEntidad(SeguimientoDto dto) {
        Seguimiento s = new Seguimiento();
        s.setEstado(dto.estado());
        s.setFecha(dto.fecha());
        return s;
    }

    // Para las listas
    // Lista de seguimientos
    public static List<SeguimientoDto> convertirSeguimientosDto(List<Seguimiento> s) {
        if (s == null)
            return List.of(); // ojo
        return s.stream()
                .map(DtoHandler::convertirSeguimientoDto)
                .collect(Collectors.toList());
    }

    // Lista de seguimientosDto

    public static List<Seguimiento> convertirSeguimientosEntidad(List<SeguimientoDto> s) {
        if (s == null)
            return List.of();
        return s.stream()
                .map(DtoHandler::convertirSeguimientoEntidad)
                .collect(Collectors.toList());
    }

    // Contenedor
    public static ContenedorDto convertirContenedorDto(Contenedor c) {
        if (c == null)
            return null;
        return new ContenedorDto(c.getId(), c.getPeso(), c.getVolumen(), c.getEstado(), c.getCostoVolumen());
    }

    public static Contenedor convertirContenedorEntidad(ContenedorDto dto) {
        Contenedor c = new Contenedor();
        if (dto.id() != null) {
            c.setId(dto.id());
        }
        c.setPeso(dto.peso());
        c.setVolumen(dto.volumen());
        c.setEstado(dto.estado());
        c.setCostoVolumen(dto.costoVolumen());
        return c;
    }

    // Ubicacion

    public static UbicacionDto convertirUbicacionDto(Ubicacion u) {
        if (u == null)
            return null;
        return new UbicacionDto(u.getId(), u.getNombre(), u.getTipo(), u.getLatitud(), u.getLongitud(), u.getCosto());
    }

    public static Ubicacion convertirUbicacionEntidad(UbicacionDto dto) {
        if (dto == null)
            return null;
        Ubicacion u = new Ubicacion();
        if (dto.id() != null) {
            u.setId(dto.id());
        }
        u.setNombre(dto.nombre());
        u.setTipo(dto.tipo());
        u.setLatitud(dto.latitud());
        u.setLongitud(dto.longitud());
        u.setCosto(dto.costo());
        return u;
    }

    // Ruta

    public static RutaDto convertirRutaDto(Ruta r) {
        if (r == null)
            return null;
        return new RutaDto(r.getId(), convertirSolicitudDto(r.getSolicitud()), r.getCantidadTramos(),
                r.getCantidadDepositos(), r.getCostoPorTramo(), null, r.getDistanciaTotal());
    }

    public static Ruta convertirRutaEntidad(RutaDto dto) {
        if (dto == null)
            return null;
        Ruta r = new Ruta();
        r.setSolicitud(convertirSolicitudEntidad(dto.solicitud()));
        r.setCantidadTramos(dto.cantidadTramos());
        r.setCantidadDepositos(dto.cantidadDepositos());
        r.setCostoPorTramo(dto.costoPorTramo());
        r.setDistanciaTotal(dto.distanciaTotal());
        return r;
    }

    // Tramo

    public static TramoDto convertirTramoDto(Tramo t) {
        if (t == null)
            return null;
        return new TramoDto(
                t.getId(),
                convertirUbicacionDto(t.getOrigen()),
                convertirUbicacionDto(t.getDestino()),
                convertirCamionDto(t.getCamion()),
                convertirRutaDto(t.getRuta()),
                t.getTipo(),
                t.getEstado(),
                t.getCostoAproximado(),
                t.getCostoReal(),
                t.getFechaHoraInicio(),
                t.getFechaHoraFin(),
                t.getDistancia());
    }

    public static CamionDto convertirCamionDto(Camion c) {
        if (c == null)
            return null;
        return new CamionDto(c.getId(), c.getPatente(), c.getNombreTransportista(), c.getTelefono(),
                c.getCapacidadPesoKg(), c.getCapacidadVolumenM3(), c.getCostoPorKm(), c.getConsumoCombustibleLx100km(),
                c.getDisponible());
    }

    public static Camion convertirCamionEntidad(CamionDto dto) {
    if (dto == null)
        return null;

    Camion c = new Camion();
    c.setId(dto.id());
    c.setPatente(dto.patente());
    c.setNombreTransportista(dto.nombreTransportista());
    c.setTelefono(dto.telefono());
    c.setCapacidadPesoKg(dto.capacidadPesoKg());
    c.setCapacidadVolumenM3(dto.capacidadVolumenM3());
    c.setCostoPorKm(dto.costoPorKm());
    c.setConsumoCombustibleLx100km(dto.consumoCombustibleLx100km());
    c.setDisponible(dto.disponible());

    return c;
}

    public static Tramo convertirTramoEntidad(TramoDto dto) {
        if (dto == null)
            return null;

        Tramo t = new Tramo();
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

    public static List<TramoDto> convertirTramosDto(List<Tramo> t) {
        if (t == null)
            return List.of();
        return t.stream()
                .map(DtoHandler::convertirTramoDto)
                .collect(Collectors.toList());
    }

    public static List<Tramo> convertirTramosEntidad(List<TramoDto> t) {
        if (t == null)
            return List.of();
        return t.stream()
                .map(DtoHandler::convertirTramoEntidad)
                .collect(Collectors.toList());
    }

    // Cliente

    public static ClienteDto convertirClienteDto(Cliente c) {
        if (c == null)
            return null;
        return new ClienteDto(c.getId(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getDireccion(), c.getDni());
    }

    public static Cliente convertirClienteEntidad(ClienteDto dto) {
        Cliente c = new Cliente();
        if (dto.id() != null) {
            c.setId(dto.id());
        }
        c.setNombre(dto.nombre());
        c.setApellido(dto.apellido());
        c.setTelefono(dto.telefono());
        c.setDireccion(dto.direccion());
        c.setDni(dto.dni());
        return c;
    }

    // Solicitud
    public static SolicitudDto convertirSolicitudDto(Solicitud s) {
        if (s == null)
            return null;

        return new SolicitudDto(
                s.getId(),
                s.getEstado(),
                s.getCostoEstimado(),
                s.getTiempoEstimado(),
                s.getTiempoReal(),
                s.getCostoFinal(),
                convertirClienteDto(s.getCliente()),
                convertirContenedorDto(s.getContenedor()),
                convertirUbicacionDto(s.getOrigen()),
                convertirUbicacionDto(s.getDestino()),
                convertirSeguimientosDto(s.getSeguimiento()));
    }

    public static Solicitud convertirSolicitudEntidad(SolicitudDto dto) {
        if (dto == null)
            return null;

        Solicitud s = new Solicitud();
        if (dto.id() != null) {
            s.setId(dto.id());
        }
        s.setEstado(dto.estado());
        s.setCostoEstimado(dto.costoEstimado());
        s.setTiempoEstimado(dto.tiempoEstimado());
        s.setTiempoReal(dto.tiempoReal());
        s.setCostoFinal(dto.costoFinal());
        s.setCliente(convertirClienteEntidad(dto.cliente()));
        s.setContenedor(convertirContenedorEntidad(dto.contenedor()));
        s.setOrigen(convertirUbicacionEntidad(dto.origen()));
        s.setDestino(convertirUbicacionEntidad(dto.destino()));
        s.setSeguimiento(convertirSeguimientosEntidad(dto.seguimiento()));

        return s;
    }
}
