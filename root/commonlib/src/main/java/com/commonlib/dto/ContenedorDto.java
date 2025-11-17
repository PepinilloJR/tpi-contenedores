package com.commonlib.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record ContenedorDto(
        Long id,
        @NotNull(message = "El peso es obligatorio")
        @Positive(message = "El peso debe ser mayor a 0")
        Double peso,
        @NotNull(message = "El volumen es obligatorio")
        @Positive(message = "El volumen debe ser mayor a 0")
        Double volumen,
        // Se puede borrar
        String estado,
        @NotNull(message = "El costo de volumen es obligatorio")
        @Positive(message = "El costo de volumen debe ser mayor a 0")
        Double costoVolumen
        ) {
}
