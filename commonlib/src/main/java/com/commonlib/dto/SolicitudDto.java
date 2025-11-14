package com.commonlib.dto;

public record SolicitudDto(
        Long id,
        Double costoEstimado,
        Double tiempoEstimado,
        Double tiempoReal,
        Double costoFinal, 
        ClienteDto clienteDto,
        ContenedorDto contenedorDto,
        RutaDto rutaDto
        ) {
}