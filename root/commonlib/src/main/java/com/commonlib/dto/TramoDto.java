package com.commonlib.dto;

import java.time.LocalDateTime;


public record TramoDto(
        Long id,
        UbicacionDto origen,
        UbicacionDto destino,
        CamionDto camion,
        RutaDto ruta,
        String tipo,
        String estado,
        Double costoAproximado,
        Double costoReal,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        Double distancia) {

}