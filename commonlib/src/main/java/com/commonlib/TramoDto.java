package com.commonlib;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TramoDto(Long id,
        String origen,
        String destino,
        String tipo,
        String estado,
        BigDecimal costoAproximado,
        BigDecimal costoReal,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin
        ) {
}