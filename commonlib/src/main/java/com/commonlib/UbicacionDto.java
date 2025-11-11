package com.commonlib;

public record UbicacionDto(
        Long id,
        String nombre,
        Double latitud,
        Double longitud
        ) {
}
