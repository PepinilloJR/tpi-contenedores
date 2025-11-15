package com.commonlib.dto;

public record ContenedorDto(
        Long id,
        Double peso,
        Double volumen,
        String estado,
        Double costoVolumen
        ) {
}
