package com.pedidos.service.demo.dto;

import java.time.LocalDateTime;

public record TramoDtoIn(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer combustibleConsumido,
    Long idCamion,
    Double costoAproximado,
    Double costoReal

) {
    
}
