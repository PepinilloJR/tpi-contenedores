package com.commonlib.dto;

public record SolicitudDto(
                Long id,
                String estado,
                Double costoEstimado,
                Double tiempoEstimado,
                Double tiempoReal,
                Double costoFinal,
                ClienteDto cliente,
                ContenedorDto contenedor,
                UbicacionDto origen,
                UbicacionDto destino) {
}