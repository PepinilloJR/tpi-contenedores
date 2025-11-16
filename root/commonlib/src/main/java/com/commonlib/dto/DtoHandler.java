package com.commonlib.dto;

import com.commonlib.entidades.Camion;
import com.commonlib.entidades.Cliente;
import com.commonlib.entidades.Contenedor;
import com.commonlib.entidades.Ruta;
import com.commonlib.entidades.Solicitud;
import com.commonlib.entidades.Tramo;
import com.commonlib.entidades.Ubicacion;

public interface DtoHandler {

    // Contenedor
    public static ContenedorDto convertirContenedorDto(Contenedor c) {
        if (c == null)
            return null;
        return new ContenedorDto(c.getId(), c.getPeso(), c.getVolumen(), c.getEstado(), c.getCostoVolumen());
    }

    public static Contenedor convertirContenedorEntidad(ContenedorDto dto) {
        Contenedor c = new Contenedor();
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
        return new UbicacionDto(u.getId(), u.getNombre(), u.getTipo(), u.getLatitud(), u.getLongitud());
    }

    public static Ubicacion convertirUbicacionEntidad(UbicacionDto dto) {
        if (dto == null)
            return null;
        Ubicacion u = new Ubicacion();
        u.setNombre(dto.nombre());
        u.setTipo(dto.tipo());
        u.setLatitud(dto.latitud());
        u.setLongitud(dto.longitud());
        return u;
    }

    // Ruta

    public static RutaDto convertirRutaDto(Ruta r) {
        if (r == null)
            return null;
<<<<<<< HEAD
        return new RutaDto(r.getId(), convertirSolicitudDto(r.getSolicitud()), r.getCantidadTramos(), r.getCantidadDepositos(), r.getCostoPorTramo(), null);
=======
        return new RutaDto(r.getId(), convertirSolicitudDto(r.getSolicitud()), r.getCantidadTramos(),
                r.getCantidadDepositos(), r.getCostoPorTramo());
>>>>>>> e66c7b78fe9293126e9785f6a03f63220380ba78
    }

    public static Ruta convertirRutaEntidad(RutaDto dto) {
        if (dto == null)
            return null;
        Ruta r = new Ruta();
        r.setSolicitud(convertirSolicitudEntidad(dto.solicitudDto()));
        r.setCantidadTramos(dto.cantidadTramos());
        r.setCantidadDepositos(dto.depositosID().length);
        r.setCostoPorTramo(dto.costoPorTramo());
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
                t.getFechaHoraFin());
    }

    public static CamionDto convertirCamionDto(Camion c) {
        if (c == null)
            return null;
        return new CamionDto(c.getId(), c.getPatente(), c.getNombreTransportista(), c.getTelefono(),
                c.getCapacidadPesoKg(), c.getCapacidadVolumenM3(), c.getCostoPorKm(), c.getConsumoCombustibleLx100km(),
                c.getDisponible());
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

    // Cliente

    public static ClienteDto convertirClienteDto(Cliente c) {
        if (c == null)
            return null;
        return new ClienteDto(c.getId(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getDireccion(), c.getDni());
    }

    public static Cliente convertirClienteEntidad(ClienteDto dto) {
        Cliente c = new Cliente();
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
                convertirUbicacionDto(s.getDestino()));
    }

    public static Solicitud convertirSolicitudEntidad(SolicitudDto dto) {
        if (dto == null)
            return null;

        Solicitud s = new Solicitud();
        s.setEstado(dto.estado());
        s.setCostoEstimado(dto.costoEstimado());
        s.setTiempoEstimado(dto.tiempoEstimado());
        s.setTiempoReal(dto.tiempoReal());
        s.setCostoFinal(dto.costoFinal());
        s.setCliente(convertirClienteEntidad(dto.cliente()));
        s.setContenedor(convertirContenedorEntidad(dto.contenedor()));
        s.setOrigen(convertirUbicacionEntidad(dto.origen()));
        s.setDestino(convertirUbicacionEntidad(dto.destino()));

        return s;
    }
}
