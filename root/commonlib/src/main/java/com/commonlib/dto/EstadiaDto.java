package com.commonlib.dto;

import java.time.LocalDateTime;

public record EstadiaDto(
    Long idEstadia, 
    TramoDto tramo,            // Necesitamos el ID para crear la relación              // FK al Microservicio de Tramos/Rutas
    LocalDateTime fechaHoraEntrada,
    LocalDateTime fechaHoraSalida
) {
}