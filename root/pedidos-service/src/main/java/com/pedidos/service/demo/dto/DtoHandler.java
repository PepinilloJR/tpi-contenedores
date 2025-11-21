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
}
