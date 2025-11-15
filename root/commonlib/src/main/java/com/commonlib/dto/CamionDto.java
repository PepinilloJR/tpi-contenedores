package com.commonlib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CamionDto(
        Long id,

        @NotBlank(message = "La patente es obligatoria")
        String patente,

        @NotBlank(message = "El nombre del transportista es obligatorio")
        String nombreTransportista,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotNull(message = "La capacidad de peso es obligatoria")
        @Positive(message = "La capacidad de peso debe ser mayor que cero")
        Double capacidadPesoKg,

        @NotNull(message = "La capacidad de volumen es obligatoria")
        @Positive(message = "La capacidad de volumen debe ser mayor que cero")
        Double capacidadVolumenM3,

        @NotNull(message = "El costo por kilómetro es obligatorio")
        @Positive(message = "El costo por kilómetro debe ser mayor que cero")
        Double costoPorKm,

        @NotNull(message = "El consumo de combustible es obligatorio")
        @Positive(message = "El consumo de combustible debe ser mayor que cero")
        Double consumoCombustibleLx100km,

        Boolean disponible
) {}
