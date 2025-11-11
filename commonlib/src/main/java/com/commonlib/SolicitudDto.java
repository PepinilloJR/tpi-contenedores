package com.commonlib;

public record SolicitudDto(
        Long id,
        Double costoEstimado,
        Double tiempoEstimado,
        Double tiempoReal,
        Double costoFinal, 
        ClienteDto clienteDto,
        ContenedorDto contenedorDto
        ) {
}