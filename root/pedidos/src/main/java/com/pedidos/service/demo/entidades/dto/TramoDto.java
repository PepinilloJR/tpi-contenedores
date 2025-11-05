package com.pedidos.service.demo.entidades.dto;

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
