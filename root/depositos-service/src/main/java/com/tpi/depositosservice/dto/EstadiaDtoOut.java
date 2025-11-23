package com.tpi.depositosservice.dto;

import java.time.LocalDateTime;

public record EstadiaDtoOut(
        Long id,
        Long idDeposito,
        Long idContenedor,
        LocalDateTime fechaHoraEntrada,
        LocalDateTime fechaHoraSalida,
        Double costo
) {

}
