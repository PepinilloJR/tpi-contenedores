package com.commonlib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CamionDto(
        Long id,

        @NotNull(message = "La tarifa es obligatoria")
        TarifaDto tarifa,

        @NotBlank(message = "La patente es obligatoria")
        String patente,

        @NotBlank(message = "El nombre del transportista es obligatorio")
        String nombreTransportista,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefonoTransportista,

        @NotNull(message = "La capacidad de peso es obligatoria")
        @Positive(message = "La capacidad de peso debe ser mayor que cero")
        Double capacidadPeso,

        @NotNull(message = "La capacidad de volumen es obligatoria")
        @Positive(message = "La capacidad de volumen debe ser mayor que cero")
        Double capacidadVolumen,

        @NotNull(message = "El costo por kilómetro es obligatorio")
        @Positive(message = "El costo por kilómetro debe ser mayor que cero")
        Double consumoCombustiblePromedio,

        Boolean disponible
) {}
