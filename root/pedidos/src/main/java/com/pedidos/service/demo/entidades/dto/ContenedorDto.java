package com.pedidos.service.demo.entidades.dto;

public record ContenedorDto(
        Long id,
        Double peso,
        Double volumen,
        String estado,
        ClienteDto clienteDto
        ) {
}
