package com.commonlib.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeguimientoDto(
        Long id,
        @NotBlank(message = "El estado es obligatorio")
        String estado,
        @NotNull(message = "La fecha es obligatoria")
        LocalDateTime fecha) {

}
