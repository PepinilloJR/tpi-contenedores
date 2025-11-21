package com.pedidos.service.demo.dto;

public record ClienteDto(
        Long id,
        String nombre,
        String apellido,
        String telefono,
        String direccion,
        Integer dni) {

}
