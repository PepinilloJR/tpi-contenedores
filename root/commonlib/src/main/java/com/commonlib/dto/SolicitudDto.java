package com.commonlib.dto;

import java.util.List;

public record SolicitudDto(
        Long id,
        String estado,
        Double costoEstimado,
        Double tiempoEstimado,
        Long tiempoReal,
        Double costoFinal,
        ClienteDto cliente,
        ContenedorDto contenedor,
        UbicacionDto origen,
        UbicacionDto destino,
        List<SeguimientoDto> seguimiento) {
}