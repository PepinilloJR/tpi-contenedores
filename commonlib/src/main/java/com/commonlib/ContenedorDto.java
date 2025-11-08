package com.commonlib;

public record ContenedorDto(
        Long id,
        Double peso,
        Double volumen,
        String estado,
        ClienteDto clienteDto
        ) {
}
