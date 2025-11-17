package com.commonlib.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record TarifaDto(

        Long id,

        @NotNull(message = "El costo por litro es obligatorio")
        @PositiveOrZero(message = "El costo por litro no puede ser negativo")
        Double costoLitro,

        @NotNull(message = "El costo por volumen es obligatorio")
        @PositiveOrZero(message = "El costo por volumen no puede ser negativo")
        Double costoVolumen,

        @NotNull(message = "El costo por kilometro es obligatorio")
        @PositiveOrZero(message = "El costo por kilómetro no puede ser negativo")
        Double costoKilometro

) {}
