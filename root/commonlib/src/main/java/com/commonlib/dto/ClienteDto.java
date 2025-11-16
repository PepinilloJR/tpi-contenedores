package com.commonlib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ClienteDto(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "El telefono es obligatorio") String telefono,
        @NotBlank(message = "La direccion es obligatoria") String direccion,
        @NotNull(message = "El dni es obligatorio") Integer dni) {
}
