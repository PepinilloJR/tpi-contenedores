package com.commonlib.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteDto(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "El telefono es obligatorio") String telefono,
        @NotBlank(message = "La direccion es obligatoria") String direccion,
        @NotBlank(message = "El dni es obligatorio") Integer dni) {
}
