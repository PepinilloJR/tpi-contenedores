package com.commonlib.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TramoDto(
        Long id,
        UbicacionDto origen,
        UbicacionDto destino,
        CamionDto camionDto,
        RutaDto ruta,
        String tipo,
        String estado,
        BigDecimal costoAproximado,
        BigDecimal costoReal,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        Double distancia) {
}