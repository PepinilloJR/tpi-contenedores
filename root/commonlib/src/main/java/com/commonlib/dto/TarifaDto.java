package com.commonlib.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record TarifaDto(

        Long id,

        @NotBlank(message = "El nombre de la tarifa es obligatorio")
        @Size(max = 80, message = "El nombre no puede tener más de 80 caracteres")
        String nombre,

        @NotNull(message = "El precio por kilómetro es obligatorio")
        @PositiveOrZero(message = "El precio por kilómetro no puede ser negativo")
        Double precioPorKm,

        @PositiveOrZero(message = "El precio fijo no puede ser negativo")
        Double precioFijo,

        @Size(max = 10, message = "La moneda no puede tener más de 10 caracteres")
        String moneda,

        @NotNull(message = "La fecha de vigencia desde es obligatoria")
        LocalDate vigenciaDesde,

        LocalDate vigenciaHasta,

        Boolean activo
) {}
