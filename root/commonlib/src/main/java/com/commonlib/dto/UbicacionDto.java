package com.commonlib.dto;

public record UbicacionDto(
        Long id,
        String nombre,
        String tipo,
        Double latitud,
        Double longitud
        ) {
}
