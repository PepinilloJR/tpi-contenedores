package com.commonlib.dto;

public record SolicitudDto(
                Long id,
                String estado,
                Double costoEstimado,
                Double tiempoEstimado,
                Double tiempoReal,
                Double costoFinal,
                ClienteDto clienteDto,
                ContenedorDto contenedorDto) {
}