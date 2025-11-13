package com.tpi.depositosservice.dto;

import java.time.LocalDateTime;

public record EstadiaDto(
    Long idEstadia,
    Long idDeposito,             // Necesitamos el ID para crear la relación
    Long idTramo,                // FK al Microservicio de Tramos/Rutas
    LocalDateTime fechaHoraEntrada,
    LocalDateTime fechaHoraSalida
) {
}