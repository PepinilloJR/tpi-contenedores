package com.pedidos.service.demo.dto;

import com.commonlib.entidades.*;

public interface DtoHandler {
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

    public static SolicitudDtoOut convertirSolicitudDtoOut(Solicitud s) {
        if (s == null)
            return null;
        return new SolicitudDtoOut(
                s.getId(),
                s.getEstado(),
                s.getCostoFinal(),
                s.getCliente() != null ? s.getCliente().getId() : null,
                s.getContenedor() != null ? s.getContenedor().getId() : null,
                s.getOrigen() != null ? s.getOrigen().getId() : null,
                s.getDestino() != null ? s.getDestino().getId() : null);
    }

    public static SeguimientoDtoOut convertirSeguimientoDtoOut(Seguimiento seg) {
        if (seg == null)
            return null;
        return new SeguimientoDtoOut(
                seg.getEstadoAnterior(),
                seg.getFecha(),
                seg.getComentario());
    }
}
