package com.commonlib.dto;

public record UbicacionDto(
        Long id,
        String nombre,
        Double latitud,
        Double longitud
        ) {
}
